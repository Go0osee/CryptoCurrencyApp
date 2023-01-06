package com.go0ose.cryptocurrencyapp.data.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URl = "https://api.coingecko.com/api/v3/"
    const val USD = "usd"

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun getCryptoClient() = Retrofit.Builder()
        .baseUrl(BASE_URl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getCryptoApi(): CryptoApi = getCryptoClient().create(CryptoApi::class.java)
}
