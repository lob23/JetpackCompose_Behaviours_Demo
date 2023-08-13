package com.example.jetpackcomposebehaviourdemo.Media_Playback_Demo

import android.net.Uri
import androidx.media3.common.MediaItem

data class VideoProperties(
    val contentUri: Uri,
    val mediaItem: MediaItem,
    val name: String
)
