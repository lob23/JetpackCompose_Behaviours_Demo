package com.example.jetpackcomposebehaviourdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jetpackcomposebehaviourdemo.Notification_Demo.Notification_screen
import com.example.jetpackcomposebehaviourdemo.Permission_Demo.PermissionScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // startActivity(Intent(this, DownloadManagerDemo::class.java))
        // startActivity(Intent(this, Notification_screen::class.java))
        startActivity(Intent(this, Notification_screen::class.java))
    }
}