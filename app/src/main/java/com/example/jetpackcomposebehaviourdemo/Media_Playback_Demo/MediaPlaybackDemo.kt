package com.example.jetpackcomposebehaviourdemo.Media_Playback_Demo


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import com.example.jetpackcomposebehaviourdemo.Theme.VideoPlayerComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaPlaybackDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoPlayerComposeTheme {
                val viewModel = hiltViewModel<ViewModelManager>()
                val videoItems by viewModel.video_properties.collectAsState()
                val selector = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
                onResult = {uri -> uri?.let { viewModel::addVideoUri } })

                var lifecycle by remember{
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner){
                    val observer = LifecycleEventObserver { _, event ->
                        lifecycle = event
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    AndroidView(
                        factory = {
                        context->PlayerView(context).also {
                            it.player = viewModel.player
                        }
                    },
                    update={
                           when(lifecycle){
                               Lifecycle.Event.ON_PAUSE->{
                                   it.onPause()
                                   it.player?.pause()
                               }
                               Lifecycle.Event.ON_RESUME->{
                                   it.onResume()
                                   it.player?.pause()
                               }
                               else ->Unit
                           }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = {
                        selector.launch("video/mp4")
                    }) {
                        Icon(imageVector = Icons.Default.Face, contentDescription = "Select")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth()){
                        items(videoItems){ item ->
                            androidx.compose.material3.Text(
                                text = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.playVideo(item.contentUri)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
