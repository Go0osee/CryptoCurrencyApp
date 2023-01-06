package com.go0ose.cryptocurrencyapp.domain

import com.go0ose.cryptocurrencyapp.presentation.model.Coin

interface CryptoInteractor {

    suspend fun getCryptoListFromApi(order: String, page: Int): List<Coin>

    suspend fun insertCryptoListToDataBase(cryptoList: List<Coin>)

    suspend fun getCryptoListFromDataBase(): List<Coin>
}