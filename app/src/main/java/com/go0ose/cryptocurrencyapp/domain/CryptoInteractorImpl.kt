package com.go0ose.cryptocurrencyapp.domain

import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.presentation.model.CoinDetails
import com.go0ose.cryptocurrencyapp.utils.toCoin
import com.go0ose.cryptocurrencyapp.utils.toCoinDetails
import com.go0ose.cryptocurrencyapp.utils.toCryptoEntity

class CryptoInteractorImpl(
    private val cryptoRepository: CryptoRepository
) : CryptoInteractor {

    override suspend fun getCryptoListFromApi(order: String, page: Int): List<Coin> {
        val response = cryptoRepository.getCryptoListFromApi(order, page)

        return if (response.isSuccessful) {
            response.body()?.map { cryptoResponse ->
                cryptoResponse.toCoin()
            }.orEmpty()
        } else {
            throw Throwable(response.errorBody().toString())
        }
    }

    override suspend fun insertCryptoListToDataBase(cryptoList: List<Coin>) {
        cryptoRepository.insertCryptoListToDataBase(
            cryptoList.map { coin ->
                coin.toCryptoEntity()
            }
        )
    }

    override suspend fun getCryptoListFromDataBase(): List<Coin> {
        return cryptoRepository.getCryptoListFromDataBase().map { cryptoEntity ->
            cryptoEntity.toCoin()
        }
    }

    override suspend fun getCoinDetailsFromApi(id: String, days: String): CoinDetails {

        val response = cryptoRepository.getMarketChartFromApi(id, days)

        return if (response.isSuccessful) {
            response.body()?.toCoinDetails()!!
        } else {
            throw Throwable(response.errorBody().toString())
        }
    }
}