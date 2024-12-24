package com.myjar.jarassignment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()


    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("test_screen") {
            Text("This is my test screen")
        }
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { selectedItem ->
                    navController.navigate("item_detail/$selectedItem")
                },
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
) {
    val items by viewModel.listStringData.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TextField(
                value = searchQuery,
                onValueChange = {
                    if (it.isNotBlank()) {
                        viewModel.onQueryChange(it)
                    }
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        if (searchQuery.isNotBlank()) {
            if (searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(searchResults) { item ->
                        ItemCard(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text(text = "No items available for the searched query")
            }
        } else if (items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onNavigateToDetail(item.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Text(text = "No items available")
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = item.name, fontWeight = FontWeight.Bold, color = Color.Black)
        item.data?.let {
            it.color?.let {
                Text(text = it, fontWeight = FontWeight.Normal, color = Color.DarkGray)
            }
            it.price?.let {
                Text(text = it.toString(), fontWeight = FontWeight.Normal, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun ItemDetailScreen(itemId: String?) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository
    Text(
        text = "Item Details for ID: $itemId",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
