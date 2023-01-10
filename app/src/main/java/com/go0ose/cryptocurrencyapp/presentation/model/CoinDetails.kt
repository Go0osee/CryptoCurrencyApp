package com.go0ose.cryptocurrencyapp.presentation.model

import com.github.mikephil.charting.data.Entry

data class CoinDetails(
    val changePercentage: String,
    val maxPrice: String,
    val minPrice: String,
    val listEntry: List<Entry>
)