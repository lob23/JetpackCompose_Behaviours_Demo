package com.example.jetpackcomposebehaviourdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcomposebehaviourdemo.ui.theme.NotificationPermissionsTheme

class PermissionScreen : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
    )

    // This method is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // This is a custom theme that sets the colors and fonts for the permission dialogs.
            NotificationPermissionsTheme() {
                // Get a reference to the view model.
                val viewModel = viewModel<MainViewModel>()

                // This is a queue of permissions that the user has been asked to grant, but has not yet granted.
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                // This is a launcher that will be used to request a single permission from the user.
                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        // Update the view model with the result of the permission request.
                        viewModel.onPermissionResult(
                            permission = Manifest.permission.CAMERA,
                            isGranted = isGranted
                        )
                    }
                )

                // This is a launcher that will be used to request multiple permissions from the user.
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        // Update the view model with the results of the permission requests.
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                // This column contains two buttons: one to request a single permission and one to request multiple permissions.
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // This button requests the camera permission from the user.
                    Button(onClick = {
                        cameraPermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }) {
                        Text(text = "Request one permission")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // This button requests the record audio and call phone permissions from the user.
                    Button(onClick = {
                        multiplePermissionResultLauncher.launch(permissionsToRequest)
                    }) {
                        Text(text = "Request multiple permission")
                    }
                }

                // This loop iterates through the queue of permissions and shows a permission dialog for each one.
                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        // Get the text provider for the specified permission.
                        val permissionTextProvider = when (permission) {
                            Manifest.permission.CAMERA -> {
                                CameraPermissionTextProvider()
                            }
                            Manifest.permission.RECORD_AUDIO -> {
                                RecordAudioPermissionTextProvider()
                            }
                            Manifest.permission.CALL_PHONE -> {
                                PhoneCallPermissionTextProvider()
                            }
                            else -> return@forEach
                        }

                        // Show the permission dialog.
                        PermissionDialog(
                            permissionTextProvider = permissionTextProvider,
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                // Dismiss the dialog and request the permission again.
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }
            }
        }
    }
}


fun Activity.openAppSettings() {
    // Create an intent to open the app settings screen.
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    // Start the intent.
    startActivity(intent)
}