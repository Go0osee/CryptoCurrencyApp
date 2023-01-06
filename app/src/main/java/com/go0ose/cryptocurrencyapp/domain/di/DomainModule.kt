package com.go0ose.cryptocurrencyapp.domain.di

import com.go0ose.cryptocurrencyapp.domain.CryptoInteractor
import com.go0ose.cryptocurrencyapp.domain.CryptoInteractorImpl
import org.koin.dsl.module

val domainModule =  module {

    single<CryptoInteractor> {
        CryptoInteractorImpl(
            cryptoRepository =  get()
        )
    }
}