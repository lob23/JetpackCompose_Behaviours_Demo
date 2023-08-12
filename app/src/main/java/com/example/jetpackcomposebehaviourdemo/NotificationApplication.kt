package com.example.jetpackcomposebehaviourdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val channel = NotificationChannel(
                "channel_id",
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