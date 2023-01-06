package com.go0ose.cryptocurrencyapp.data.retrofit

import com.google.gson.annotations.SerializedName

data class CryptoResponse(
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("market_cap")
    val marketCap: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
)