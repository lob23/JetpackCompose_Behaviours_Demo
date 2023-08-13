package com.example.jetpackcomposebehaviourdemo.Media_Playback_Demo

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

data class MetaData(
    val fileName:String
)
interface MetadataRetriever {
    fun getMetaDataFromUri(contentUri: Uri): MetaData? // nullable is not allowed
}

class MetaDataReaderImpl(
    private val app: Application
): MetadataRetriever {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if (contentUri.scheme != "content") {
            return null
        }


        val fileName = app.contentResolver.query(
            contentUri,
            arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use{
            cursor -> val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(index)
        }
        return fileName?.let{fullFileName -> MetaData(
            fileName = Uri.parse(fullFileName).lastPathSegment?: return null
        )
        }
    }
}