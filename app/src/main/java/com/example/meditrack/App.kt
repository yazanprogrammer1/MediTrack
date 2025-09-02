package com.example.meditrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        const val CHANNEL_ID = "alarmChannel"
        lateinit var manager: NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val channel =
            NotificationChannel(CHANNEL_ID, "Alarm channel", NotificationManager.IMPORTANCE_HIGH)
        manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}