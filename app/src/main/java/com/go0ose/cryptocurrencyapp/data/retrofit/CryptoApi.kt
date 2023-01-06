package com.go0ose.cryptocurrencyapp.data.retrofit

import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient.USD
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    @GET("coins/markets")
    suspend fun getCryptoListFromApi(
        @Query("vs_currency") vsCurrency: String = USD,
        @Query("order") order: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean = false
    ): Response<List<CryptoResponse>>
}