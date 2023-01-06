package com.go0ose.cryptocurrencyapp.data.di

import com.go0ose.cryptocurrencyapp.data.retrofit.CryptoApi
import com.go0ose.cryptocurrencyapp.data.retrofit.RetrofitClient
import org.koin.dsl.module

val retrofitModule = module {

    single<CryptoApi> {
        RetrofitClient.getCryptoApi()
    }
}