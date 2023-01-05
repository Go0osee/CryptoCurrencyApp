package com.go0ose.cryptocurrencyapp

import android.app.Application
import com.go0ose.cryptocurrencyapp.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoApplication)
            modules(
                listOf(
                    viewModelModule
                )
            )
        }
    }
}
