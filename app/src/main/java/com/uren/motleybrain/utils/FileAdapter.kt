package com.uren.motleybrain.utils

import android.os.Build
import android.os.Environment
import android.util.Log

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import com.uren.motleybrain.Constants.CustomConstants.APP_NAME

object FileAdapter {

    val cropMediaFile: File?
        get() {
            val mediaFile: File
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(APP_NAME),
                Environment.DIRECTORY_PICTURES
            )

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(APP_NAME, "failed to create directory")
                    return null
                }
            }
            mediaFile = File(mediaStorageDir.path + File.separator + "IMG_CROP.jpg")
            return mediaFile
        }

    fun getOutputMediaFile(type: Int): File? {

        var directoryChild: String? = null

        when (type) {
            MEDIA_TYPE_IMAGE -> directoryChild = Environment.DIRECTORY_PICTURES

            MEDIA_TYPE_VIDEO -> directoryChild = Environment.DIRECTORY_MOVIES
            MEDIA_TYPE_AUDIO -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                directoryChild = Environment.DIRECTORY_DOCUMENTS
            }
            else -> {
            }
        }

        if (directoryChild != null && !directoryChild.isEmpty()) {

            val mediaStorageDir =
                File(Environment.getExternalStoragePublicDirectory(APP_NAME), directoryChild)

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(APP_NAME, "failed to create directory")
                    return null
                }
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator +
                            "IMG_" + timeStamp + ".jpg"
                )
                return mediaFile
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator +
                            "VID_" + timeStamp + ".mp4"
                )
                return mediaFile
            } else {
                mediaFile = File(
                    mediaStorageDir.path + File.separator +
                            "AUD_" + timeStamp + ".mp3"
                )
                return mediaFile
            }
        }

        return null
    }
}
