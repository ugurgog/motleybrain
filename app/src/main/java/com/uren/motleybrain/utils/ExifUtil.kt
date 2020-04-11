package com.uren.motleybrain.utils

import android.graphics.Bitmap
import android.graphics.Matrix

import androidx.exifinterface.media.ExifInterface

object ExifUtil {

    fun rotateImageIfRequired(photoPath: String?, bitmap: Bitmap?): Bitmap? {

        val ei: ExifInterface
        val rotatedBitmap: Bitmap

        if (bitmap == null || photoPath == null)
            return null

        try {
            ei = ExifInterface(photoPath)
        } catch (e: Exception) {
            return null
        }

        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        when (orientation) {

            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)

            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
            else -> rotatedBitmap = bitmap
        }

        return rotatedBitmap
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val bitmap: Bitmap
        val matrix = Matrix()
        matrix.postRotate(angle)
        bitmap = Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )

        if (!source.isRecycled)
            source.isRecycled
        return bitmap
    }
}