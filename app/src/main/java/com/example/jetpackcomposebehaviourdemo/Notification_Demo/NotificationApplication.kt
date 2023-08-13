package com.example.jetpackcomposebehaviourdemo.Notification_Demo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

import dagger.hilt.android.HiltAndroidApp

open class NotificationApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            Log.d("Application", "OnCreate");
            val channel = NotificationChannel(
                "channel_ID",
                "Channel_name",
                // Make notification make little sound and rank higher in notification tab
                NotificationManager.IMPORTANCE_HIGH,
            )

            // Create channel
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}