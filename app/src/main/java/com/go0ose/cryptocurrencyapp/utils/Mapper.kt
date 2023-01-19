package com.go0ose.cryptocurrencyapp.utils

import android.content.Context
import com.github.mikephil.charting.data.Entry
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoResponse
import com.go0ose.cryptocurrencyapp.data.retrofit.MarketChart
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails

fun CryptoResponse.toCoin() =
    Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol.uppercase(),
        currentPrice = this.currentPrice,
        image = this.image,
        marketCap = this.marketCap,
    )

fun Coin.toCryptoEntity() =
    CryptoEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol.uppercase(),
        currentPrice = this.currentPrice,
        image = this.image,
        marketCap = this.marketCap,
    )

fun CryptoEntity.toCoin() =
    Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol.uppercase(),
        currentPrice = this.currentPrice,
        image = this.image,
        marketCap = this.marketCap,
    )

fun MarketChart.toCoinDetails(): CoinDetails {
    return CoinDetails(
        changePercentage = changePercentage(this.prices.last().last(), this.prices.first().last()),
        maxPrice = this.prices.maxOfOrNull { list -> list[1] }!!,
        minPrice = this.prices.minOfOrNull { list -> list[1] }!!,
        listEntry = this.prices.toListEntry()
    )
}

fun formatMarketCap(num: Double): String {
    val billion = 1_000_000_000.0
    val million = 1_000_000.0
    val thousand = 1_000.0

    return when {
        num >= billion -> "%.2fB".format(num / billion)
        num >= million -> "%.2fM".format(num / million)
        num >= thousand -> "%.2fT".format(num / thousand)
        else -> "%.2f".format(num)
    }
}

fun changePercentage(newValue: Double, oldValue: Double): String {
    val result = ((newValue - oldValue) / oldValue) * 100

    return if (result > 0) {
        "+" + "%.2f".format(result) + " %"
    } else "%.2f".format(result) + " %"
}

fun List<List<Double>>.toListEntry(): List<Entry> {
    val result = mutableListOf<Entry>()

    this.forEach { list ->
        result.add(
            Entry(
                list[0].toFloat(),
                list[1].toFloat()
            )
        )
    }
    return result
}

fun Context.decipherError(message: String): String {
    return when (message) {
        "429" -> getString(R.string.too_many_requests)
        else -> getString(R.string.сonnection_error)
    }
}