package com.go0ose.cryptocurrencyapp.data.di

import androidx.room.Room
import com.go0ose.cryptocurrencyapp.data.storage.CryptoDao
import com.go0ose.cryptocurrencyapp.data.storage.CryptoDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {

    single<CryptoDataBase> {
        Room.databaseBuilder(
            androidContext(),
            CryptoDataBase::class.java,
            "crypto"
        ).build()
    }

    fun getCryptoDao(cryptoDataBase: CryptoDataBase) =
        cryptoDataBase.getCryptoDao()

    single<CryptoDao> {
        getCryptoDao(
            cryptoDataBase = get()
        )
    }
}