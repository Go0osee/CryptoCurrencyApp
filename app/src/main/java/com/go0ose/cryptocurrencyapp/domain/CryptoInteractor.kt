package com.go0ose.cryptocurrencyapp.domain

import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails

interface CryptoInteractor {

    suspend fun getCryptoListFromApi(order: String, page: Int): List<Coin>

    suspend fun insertCryptoListToDataBase(cryptoList: List<Coin>)

    suspend fun getCryptoListFromDataBase(): List<Coin>

    suspend fun getCoinDetailsFromApi(id: String, days: String): CoinDetails
}