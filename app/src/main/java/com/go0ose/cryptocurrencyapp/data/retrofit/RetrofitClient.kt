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
    const val SORT_BY_PRICE = "price_desc"
    const val SORT_BY_ALPHABETICALLY = "id_asc"
    const val SORT_BY_MARKET_CAP = "market_cap_desc"

    const val ONE_DAY = "1"
    const val ONE_WEEK = "7"
    const val ONE_MONTH = "30"
    const val ONE_YEAR = "365"
    const val ALL = "max"

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
