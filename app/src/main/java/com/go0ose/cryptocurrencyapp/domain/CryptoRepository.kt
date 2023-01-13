package com.go0ose.cryptocurrencyapp.domain

import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoResponse
import com.go0ose.cryptocurrencyapp.data.retrofit.MarketChart
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CryptoRepository {

    suspend fun getCryptoListFromApi(order: String, page: Int): Response<List<CryptoResponse>>

    suspend fun insertCryptoListToDataBase(cryptoList: List<CryptoEntity>)

    suspend fun getCryptoListFromDataBase(): List<CryptoEntity>

    suspend fun getMarketChartFromApi(id: String, days: String): Response<MarketChart>

    fun getFlowUser(): Flow<UserEntity>

    suspend fun insertUser(user: UserEntity)

    suspend fun updateUser(user: UserEntity)
}


