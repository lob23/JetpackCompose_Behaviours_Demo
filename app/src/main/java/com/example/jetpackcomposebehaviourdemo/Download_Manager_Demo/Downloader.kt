package com.example.jetpackcomposebehaviourdemo.Download_Manager_Demo

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class Downloader (
private val context: Context
): Download {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun DownloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setAllowedNetworkTypes (DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility (DownloadManager.Request. VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("image.jpg")
            .addRequestHeader("Authorization", "Bearer <token>")
            .setDestinationInExternalPublicDir (Environment.DIRECTORY_DOWNLOADS, "image.jpg")
        return downloadManager.enqueue(request)
    }
}