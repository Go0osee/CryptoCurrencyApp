package com.go0ose.cryptocurrencyapp.data

import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoApi
import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoResponse
import com.go0ose.cryptocurrencyapp.data.retrofit.MarketChart
import com.go0ose.cryptocurrencyapp.data.storage.CryptoDao
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import com.go0ose.cryptocurrencyapp.domain.CryptoRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CryptoRepositoryImpl(
    private val cryptoApi: CryptoApi,
    private val cryptoDao: CryptoDao
) : CryptoRepository {

    override suspend fun getCryptoListFromApi(
        order: String,
        page: Int
    ): Response<List<CryptoResponse>> {
        return cryptoApi.getCryptoListFromApi(
            order = order,
            page = page
        )
    }

    override suspend fun insertCryptoListToDataBase(cryptoList: List<CryptoEntity>) {
        cryptoDao.insertCryptoListToDataBase(cryptoList)
    }

    override suspend fun getCryptoListFromDataBase(): List<CryptoEntity> {
        return cryptoDao.getCryptoListFromDataBase()
    }

    override suspend fun getMarketChartFromApi(id: String, days: String): Response<MarketChart> {
        return cryptoApi.getMarketChartFromApi(id, days)
    }

    override fun getFlowUser(): Flow<UserEntity> {
        return cryptoDao.getFlowUser()
    }

    override suspend fun insertUser(user: UserEntity) {
        cryptoDao.insertUser(user)
    }

    override suspend fun updateUser(user: UserEntity) {
        cryptoDao.updateUser(user)
    }
}