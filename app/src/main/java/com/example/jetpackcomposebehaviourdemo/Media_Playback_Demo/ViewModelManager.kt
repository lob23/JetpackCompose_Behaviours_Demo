package com.example.jetpackcomposebehaviourdemo.Media_Playback_Demo

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewModelManager @Inject constructor(
    private val stateHandler: SavedStateHandle,
    val player: Player, //Change video playback, eg. Pause video, next video, create playlist,...
    private val metaDataRetriever: MetadataRetriever
): ViewModel() {
    private val videoUris = stateHandler.getStateFlow("videoUris", emptyList<Uri>()) //stateHandler store value of key to retrieved during the app

    val video_properties = videoUris.map { uris->
        uris.map { uri ->
            VideoProperties(
                contentUri =  uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataRetriever.getMetaDataFromUri(uri)?.fileName?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()) // catch cold flow to hot flow

    init {
        player.prepare()
    }

    // When user choode a video, call the following function to add the Uri of video into list
    fun addVideoUri(uri: Uri){
        stateHandler["videoUris"] = videoUris.value + uri // Add uri into list of uri (aka list of videos)
        player.addMediaItem(MediaItem.fromUri(uri))
    }

    // the following function responsible for playing a specific video in the playlist
    fun playVideo(uri: Uri){
        player.setMediaItem(
            video_properties.value.find { it.contentUri == uri }?.mediaItem ?: return // if the uri existed in the list, return mediaItem containing materials for the video. Otherwise, out the function immediately.

        )
    }

    //clear the list of videos when the ap[ is terminated.
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}