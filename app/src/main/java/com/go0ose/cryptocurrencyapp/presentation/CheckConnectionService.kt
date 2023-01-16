package com.go0ose.cryptocurrencyapp.presentation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.go0ose.cryptocurrencyapp.utils.isOnline
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CheckConnectionService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        launch {
            while (true) {
                if (!applicationContext.isOnline()) {
                    sendBroadcast(Intent("show_dialog"))
                }
                delay(13_000L)
            }
        }
        return START_STICKY
    }
}