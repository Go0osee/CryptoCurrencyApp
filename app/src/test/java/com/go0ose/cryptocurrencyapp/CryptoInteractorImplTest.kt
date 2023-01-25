package com.go0ose.cryptocurrencyapp

import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoResponse
import com.go0ose.cryptocurrencyapp.data.retrofit.MarketChart
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractorImpl
import com.go0ose.cryptocurrencyapp.domain.CryptoRepository
import com.go0ose.cryptocurrencyapp.presentation.model.Coin
import com.go0ose.cryptocurrencyapp.utils.toCryptoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response

class CryptoInteractorImplTest {

    private val cryptoRepository = Mockito.mock(CryptoRepository::class.java)
    private val interactor = CryptoInteractorImpl(cryptoRepository)

    @Test
    fun `getCryptoListFromApi should return a list of coins when response is successful`() =
        runBlocking {
            val expectedList = listOf(Coin(id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 5.0,
                image = "test",
                marketCap = 123.0))
            val response = Response.success(listOf(CryptoResponse(id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 5.0,
                image = "test",
                marketCap = 123.0)))
            Mockito.`when`(cryptoRepository.getCryptoListFromApi(order = "", page = 1))
                .thenReturn(response)
            val result = interactor.getCryptoListFromApi(order = "", page = 1)

            assertEquals(expectedList, result)
        }

    @Test(expected = Throwable::class)
    fun `getCryptoListFromApi should throw an exception when response is not successful`(): Unit =
        runBlocking {
            val response =
                Response.error<List<CryptoResponse>>(404, Mockito.mock(ResponseBody::class.java))
            Mockito.`when`(cryptoRepository.getCryptoListFromApi(order = "", page = 1))
                .thenReturn(response)
            interactor.getCryptoListFromApi(order = "", page = 1)
        }

    @Test
    fun `insertCryptoListToDataBase should call the repository method`() =
        runBlocking {
            val cryptoList = listOf(Coin(id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 5.0,
                image = "test",
                marketCap = 123.0))
            interactor.insertCryptoListToDataBase(cryptoList)

            Mockito.verify(cryptoRepository)
                .insertCryptoListToDataBase(cryptoList.map { it.toCryptoEntity() })
        }

    @Test
    fun `getCryptoListFromDataBase should return a list of coins`() =
        runBlocking {
            val expectedList = listOf(Coin(id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 5.0,
                image = "test",
                marketCap = 123.0))
            Mockito.`when`(cryptoRepository.getCryptoListFromDataBase())
                .thenReturn(expectedList.map { it.toCryptoEntity() })
            val result = interactor.getCryptoListFromDataBase()

            assertEquals(expectedList, result)
        }

    @Test(expected = Throwable::class)
    fun `getCoinDetailsFromApi should throw an exception when response is not successful`(): Unit =
        runBlocking {
            val response =
                Response.error<MarketChart>(404, Mockito.mock(ResponseBody::class.java))
            Mockito.`when`(cryptoRepository.getMarketChartFromApi(id = "BTC", days = "30"))
                .thenReturn(response)
            interactor.getCoinDetailsFromApi(id = "BTC", days = "30")
        }

    @Test
    fun `getUserFromDatabase should return a flow of UserEntity`() {
        val expectedFlow = MutableStateFlow(UserEntity(id = 1,
            avatar = "test",
            firstName = "first",
            lastName = "last",
            dayOfBirth = "1"))
        Mockito.`when`(cryptoRepository.getFlowUser())
            .thenReturn(expectedFlow)
        val result = interactor.getUserFromDatabase()

        assertEquals(expectedFlow, result)
    }

    @Test
    fun `insertUser should call the repository method`() =
        runBlocking {
            val user = UserEntity(id = 1,
                avatar = "test",
                firstName = "first",
                lastName = "last",
                dayOfBirth = "1")
            interactor.insertUser(user)

            Mockito.verify(cryptoRepository).insertUser(user)
        }

    @Test
    fun `updateUser should call the repository method`() =
        runBlocking {
            val user = UserEntity(id = 1,
                avatar = "test",
                firstName = "first",
                lastName = "last",
                dayOfBirth = "1")
            interactor.updateUser(user)

            Mockito.verify(cryptoRepository).updateUser(user)
        }
}