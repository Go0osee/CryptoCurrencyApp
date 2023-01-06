package com.go0ose.cryptocurrencyapp.data.storage

import androidx.room.*
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoListToDataBase(cryptoList: List<CryptoEntity>)

    @Query("SELECT * FROM crypto")
    suspend fun getCryptoListFromDataBase(): List<CryptoEntity>
}