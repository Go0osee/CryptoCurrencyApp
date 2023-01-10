package com.go0ose.cryptocurrencyapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.data.Entry
import com.go0ose.cryptocurrencyapp.data.retrofit.MarketChart
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails

fun ImageView.setImageByUrl(Url: String?) {
    Glide.with(context)
        .load(Url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun MarketChart.toCoinDetails(): CoinDetails {
    return CoinDetails(
        changePercentage = changePercentage(this.prices.last().last(), this.prices.first().last()),
        maxPrice = "%.2f".format(this.prices.maxOfOrNull { list -> list[1] }) + " $",
        minPrice = "%.2f".format(this.prices.minOfOrNull { list -> list[1] }) + " $",
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