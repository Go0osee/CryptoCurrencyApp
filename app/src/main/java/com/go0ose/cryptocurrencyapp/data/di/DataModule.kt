package com.go0ose.cryptocurrencyapp.data.di

import com.go0ose.cryptocurrencyapp.data.CryptoRepositoryImpl
import com.go0ose.cryptocurrencyapp.domain.CryptoRepository
import org.koin.dsl.module

val dataModule = module {

    single<CryptoRepository> {
        CryptoRepositoryImpl(
            cryptoApi = get(),
            cryptoDao = get()
        )
    }
}