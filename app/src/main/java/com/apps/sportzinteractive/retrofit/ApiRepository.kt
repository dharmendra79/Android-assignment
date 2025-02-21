package com.apps.sportzinteractive.retrofit

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository {
    suspend fun fetchApiData(url: String): Map<String, Any>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.fetchApiResponse(url)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("API_ERROR", "Error: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
                null
            }
        }
    }
}