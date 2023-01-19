package com.go0ose.cryptocurrencyapp.domain

import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails
import kotlinx.coroutines.flow.Flow

interface CryptoInteractor {

    suspend fun getCryptoListFromApi(order: String, page: Int): List<Coin>

    suspend fun insertCryptoListToDataBase(cryptoList: List<Coin>)

    suspend fun getCryptoListFromDataBase(): List<Coin>

    suspend fun getCoinDetailsFromApi(id: String, days: String): CoinDetails

    fun getUserFromDatabase(): Flow<UserEntity>

    suspend fun insertUser(user: UserEntity)

    suspend fun updateUser(user: UserEntity)
}