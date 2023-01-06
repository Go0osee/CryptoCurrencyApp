package com.go0ose.cryptocurrencyapp.presentation.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val image: String,
    val marketCap: Long
)