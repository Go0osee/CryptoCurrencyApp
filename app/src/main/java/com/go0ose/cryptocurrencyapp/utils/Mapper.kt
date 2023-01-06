package com.go0ose.cryptocurrencyapp.utils

import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoResponse
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity
import com.go0ose.cryptocurrencyapp.presentation.model.Coin

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