package com.go0ose.cryptocurrencyapp.data.storage

import androidx.room.*
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity
import com.go0ose.cryptocurrencyapp.data.storage.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoListToDataBase(cryptoList: List<CryptoEntity>)

    @Query("SELECT * FROM crypto")
    suspend fun getCryptoListFromDataBase(): List<CryptoEntity>

    @Query("SELECT * FROM user")
    fun getFlowUser(): Flow<UserEntity>

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Update(entity = UserEntity::class)
    suspend fun updateUser(user: UserEntity)
}