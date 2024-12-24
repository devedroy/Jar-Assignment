package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val _searchResults = MutableStateFlow<List<ComputerItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()


    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        Log.d("Tester", "calling api")
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchResults().collect {
                Log.d("Tester-collect", it.toString())
                _listStringData.value = it
                Log.d("Tester-list", _listStringData.value.toString())
            }
        }
    }

    fun onQueryChange(string: String) {
        _searchResults.value = _listStringData.value.filter { it.name.contains(string) }
    }
}