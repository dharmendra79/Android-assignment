package com.apps.sportzinteractive.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun fetchApiResponse(@Url url: String): Response<Map<String, Any>>
}