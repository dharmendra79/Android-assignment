package com.apps.sportzinteractive.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sportzinteractive.retrofit.ApiRepository
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {

    private val repository = ApiRepository()

    private val _apiResponse = MutableLiveData<Map<String, Any>?>()
    val apiResponse: LiveData<Map<String, Any>?> get() = _apiResponse

    fun fetchApiData(url: String) {
        viewModelScope.launch {
            val response = repository.fetchApiData(url)
            _apiResponse.postValue(response)
        }
    }
}