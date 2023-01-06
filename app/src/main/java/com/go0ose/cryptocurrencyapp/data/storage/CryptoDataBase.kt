package com.go0ose.cryptocurrencyapp.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.go0ose.cryptocurrencyapp.data.storage.entity.CryptoEntity

@Database(
    entities = [CryptoEntity::class],
    version = CryptoDataBase.VERSION
)
abstract class CryptoDataBase : RoomDatabase() {

    companion object {
        const val VERSION = 1
    }

    abstract fun getCryptoDao(): CryptoDao
}