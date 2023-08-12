package com.example.jetpackcomposebehaviourdemo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    /* This is a mutable list of strings that stores the permissions dialogs that are currently visible. */
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    /* This function dismisses the first dialog in the queue. */
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    /* This function is called when a permission request has been granted or denied. If the permission was denied and it is not already in the queue, it is added to the queue. */
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}