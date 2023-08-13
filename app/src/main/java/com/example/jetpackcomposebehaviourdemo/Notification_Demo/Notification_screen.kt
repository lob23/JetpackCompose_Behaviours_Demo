package com.example.jetpackcomposebehaviourdemo.Notification_Demo

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.jetpackcomposebehaviourdemo.Download_Manager_Demo.DownloadManagerDemo
import com.example.jetpackcomposebehaviourdemo.Media_Playback_Demo.MediaDemo
import com.example.jetpackcomposebehaviourdemo.Permission_Demo.PermissionScreen
import com.example.jetpackcomposebehaviourdemo.R
import com.example.jetpackcomposebehaviourdemo.ui.theme.NotificationPermissionsTheme


class Notification_screen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationPermissionsTheme {
                // This code gets the current context.
                val context = LocalContext.current

                // This code creates a mutable state variable to store the notification permission status.
                var hasNotificationPermission by remember {
                    // If the build SDK version is at least Tiramisu (Android 13), then check the notification permission status.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            // Check the notification permission status.
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else {
                        // If the build SDK version is less than Tiramisu, then the notification permission is always granted.
                        mutableStateOf(true)
                    }
                }

                // This code creates a column with two buttons.
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // This button requests the notification permission.
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            hasNotificationPermission = isGranted
                        }
                    )
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }) {
                        Text(text = "Request permission")
                    }

                    // This button shows a notification if the notification permission is granted.
                    Button(onClick = {
                        if(hasNotificationPermission) {
                            Toast.makeText(this@Notification_screen, "true", Toast.LENGTH_SHORT ).show()
                            showNotification()
                        }
                    }) {
                        Text(text = "Show notification")
                    }
                    Button(onClick = {
                        startActivity(Intent(this@Notification_screen, PermissionScreen::class.java))
                    }){
                        Text(text = "Permission Demo")
                    }
                }
            }
        }
    }

    // This code shows a notification.
    private fun showNotification() {

        // Create a notification.
        val notification = NotificationCompat.Builder(applicationContext, "channel_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hello world")
            .setContentText("This is a description")
            .build()

        // Get the notification manager.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.getNotificationChannel("channel_ID")
        if (channel != null) {
            Toast.makeText(this@Notification_screen, "Ok", Toast.LENGTH_SHORT ).show()
        } else {
            Toast.makeText(this@Notification_screen, "Diu ok", Toast.LENGTH_SHORT ).show()
        }
        // Notify the user with the notification.
        notificationManager.notify(1, notification)
    }
}