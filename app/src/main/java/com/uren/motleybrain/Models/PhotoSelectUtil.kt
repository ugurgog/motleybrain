package com.uren.motleybrain.Models

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import java.io.IOException
import java.util.Objects



import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import com.uren.motleybrain.Constants.CustomConstants.CAMERA_TEXT
import com.uren.motleybrain.Constants.CustomConstants.FROM_FILE_TEXT
import com.uren.motleybrain.Constants.CustomConstants.GALLERY_TEXT
import com.uren.motleybrain.utils.ExifUtil
import com.uren.motleybrain.utils.UriAdapter


class PhotoSelectUtil {

    var bitmap: Bitmap? = null
    var mediaUri: Uri? = null
        internal set
    var imageRealPath: String? = null
        internal set
    internal lateinit var context: Context
    internal var data: Intent? = null
    lateinit var type: String
    var isPortraitMode: Boolean = false
        internal set

    constructor() {}

    constructor(context: Context, data: Intent, type: String) {
        this.context = context
        this.data = data
        this.type = type
        routeSelection()
        setPortraitMode()
    }

    constructor(context: Context, uri: Uri, type: String) {
        this.context = context
        this.type = type
        this.mediaUri = uri
        routeSelection()
        setPortraitMode()
    }

    private fun routeSelection() {
        when (type) {
            CAMERA_TEXT -> onSelectFromCameraResult()
            GALLERY_TEXT -> onSelectFromGalleryResult()
            FROM_FILE_TEXT -> onSelectFromFileResult()
            else -> {
            }
        }
    }

    private fun onSelectFromFileResult() {
        if (mediaUri == null) return
        imageRealPath = UriAdapter.getRealPathFromURI(mediaUri!!, context)

        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeFile(imageRealPath, options)
        } catch (e: Exception) {
            return
        }

        if (bitmap == null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, mediaUri)
            } catch (e: IOException) {
                return
            }

        }

        if (imageRealPath != null && !imageRealPath!!.isEmpty())
            bitmap = ExifUtil.rotateImageIfRequired(imageRealPath, bitmap)
        else {
            imageRealPath = UriAdapter.getFilePathFromURI(context, mediaUri!!, MEDIA_TYPE_IMAGE)
            bitmap = ExifUtil.rotateImageIfRequired(imageRealPath, bitmap)
        }
    }

    fun onSelectFromGalleryResult() {
        if (data == null) return
        mediaUri = data!!.data

        if (mediaUri == null) return
        imageRealPath = UriAdapter.getPathFromGalleryUri(context, mediaUri!!)

        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeFile(imageRealPath, options)
        } catch (e: Exception) {
            return
        }

        if (bitmap == null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, mediaUri)
            } catch (e: IOException) {
                return
            }

        }

        bitmap = ExifUtil.rotateImageIfRequired(imageRealPath, bitmap)
    }

    fun onSelectFromCameraResult() {
        if (data == null) return
        mediaUri = data!!.data

        if (mediaUri == null) return
        imageRealPath = UriAdapter.getPathFromGalleryUri(context, mediaUri!!)

        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeFile(imageRealPath, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (bitmap == null)
            bitmap = Objects.requireNonNull<Bundle>(data!!.extras).get("data") as Bitmap?

        bitmap = ExifUtil.rotateImageIfRequired(imageRealPath, bitmap)
    }

    fun setPortraitMode() {
        if (bitmap == null)
            return

        val width = bitmap!!.width
        val heigth = bitmap!!.height

        isPortraitMode = heigth > width
    }
}
