package com.uren.motleybrain.utils


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

import androidx.core.content.FileProvider

import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

object IntentSelectUtil {

    val cameraIntent: Intent
        get() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val galleryIntent: Intent
        get() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            return intent
        }

    fun getGalleryIntentForVideo(context: Context): Intent {
        val videoUri = FileProvider.getUriForFile(
            context, context.packageName + ".provider",
            FileAdapter.getOutputMediaFile(MEDIA_TYPE_VIDEO)!!
        )
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.setTypeAndNormalize("video/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        return intent
    }
}