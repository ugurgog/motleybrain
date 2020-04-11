package com.uren.motleybrain.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BitmapCompat
import com.uren.motleybrain.Constants.CustomConstants.GALLERY_TEXT
import com.uren.motleybrain.Constants.CustomConstants.MAX_IMAGE_SIZE_1ANDHALFMB
import com.uren.motleybrain.Constants.CustomConstants.MAX_IMAGE_SIZE_2ANDHALFMB
import com.uren.motleybrain.Constants.CustomConstants.MAX_IMAGE_SIZE_5MB
import com.uren.motleybrain.Models.PhotoSelectUtil
import com.uren.motleybrain.utils.BitmapConversion.Companion

import java.io.IOException
import java.io.InputStream
import java.util.Objects


@SuppressLint("Registered")
class BitmapConversion : AppCompatActivity() {
    companion object {

        fun getRoundedShape(scaleBitmapImage: Bitmap, Width: Int, Height: Int): Bitmap {

            val targetBitmap: Bitmap
            targetBitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(targetBitmap)
            val path = Path()
            path.addCircle(
                (Width.toFloat() - 1) / 2,
                (Height.toFloat() - 1) / 2,
                Math.min(
                    Width.toFloat(),
                    Height.toFloat()
                ) / 2,
                Path.Direction.CCW
            )

            canvas.clipPath(path)
            canvas.drawBitmap(
                scaleBitmapImage,
                Rect(
                    0, 0, scaleBitmapImage.width,
                    scaleBitmapImage.height
                ),
                Rect(0, 0, Width, Height), null
            )

            return targetBitmap
        }

        fun getScreenShot(view: View): Bitmap {
            val bitmap: Bitmap
            view.isDrawingCacheEnabled = true
            bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return bitmap
        }

        fun setBlurBitmap(
            context: Context, view: View, drawableItem: Int, bitmapScale: Float,
            blurRadius: Float, mBitmap: Bitmap?
        ) {
            val bitmap: Bitmap
            if (mBitmap != null)
                bitmap = mBitmap
            else if (drawableItem != 0) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 1
                bitmap = BitmapFactory.decodeResource(context.resources, drawableItem, options)
            } else
                return

            val blurBitmap = BlurBuilder.blur(context, bitmap, bitmapScale, blurRadius)
            val dr = BitmapDrawable(context.resources, blurBitmap)
            view.background = dr
        }

        fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            val resizedBitmap: Bitmap

            val width = bm.width
            val height = bm.height

            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // CREATE A MATRIX FOR THE MANIPULATION
            val matrix = Matrix()
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight)

            // "RECREATE" THE NEW BITMAP
            resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false
            )

            if (!bm.isRecycled)
                bm.recycle()

            return resizedBitmap
        }


        fun getResizedBitmap2(context: Context, photoSelectUtil: PhotoSelectUtil?): Bitmap? {
            var mBitmap: Bitmap? = null
            var resizedBitmap: Bitmap? = null
            val maxByteValue: Int

            if (photoSelectUtil != null) {
                if (photoSelectUtil!!.bitmap != null)
                    mBitmap = photoSelectUtil!!.bitmap
            } else
                return null

            if (mBitmap == null) return null

            if (BitmapCompat.getAllocationByteCount(mBitmap) > MAX_IMAGE_SIZE_5MB)
                maxByteValue = MAX_IMAGE_SIZE_2ANDHALFMB
            else
                maxByteValue = MAX_IMAGE_SIZE_1ANDHALFMB

            if (BitmapCompat.getAllocationByteCount(mBitmap) > maxByteValue) {

                var i = 0.9f
                while (i > 0) {
                    resizedBitmap = Bitmap.createScaledBitmap(
                        mBitmap,
                        (mBitmap.width * i).toInt(),
                        (mBitmap.height * i).toInt(), true
                    )

                    if (BitmapCompat.getAllocationByteCount(resizedBitmap!!) < maxByteValue)
                        break
                    i = i - 0.05f
                }
            } else {
                if (photoSelectUtil!!.type != null && !photoSelectUtil!!.type.equals(GALLERY_TEXT)) {
                    var i = 1.2f
                    while (i < 20f) {
                        resizedBitmap = Bitmap.createScaledBitmap(
                            mBitmap,
                            (mBitmap.width * i).toInt(),
                            (mBitmap.height * i).toInt(), true
                        )

                        if (BitmapCompat.getAllocationByteCount(resizedBitmap!!) > maxByteValue)
                            break
                        i = i + 1.2f
                    }
                } else
                    resizedBitmap = mBitmap
            }

            return resizedBitmap
        }

        fun dp(value: Float, context: Context): Int {
            val dpValue: Int

            if (value == 0f) {
                return 0
            }
            dpValue =
                Math.ceil((context.resources.displayMetrics.density * value).toDouble()).toInt()

            return dpValue
        }

        fun getBitmapFromInputStream(
            input: InputStream, context: Context,
            width: Int, height: Int
        ): Bitmap {
            val myBitmap: Bitmap
            myBitmap = BitmapFactory.decodeStream(input)
            return getRoundedShape(myBitmap, width, height)
        }

        fun compressImage(context: Context, photoSelectUtil: PhotoSelectUtil?): Bitmap? {

            if (photoSelectUtil != null) {
                if (photoSelectUtil!!.mediaUri == null)
                    return null
            }

            var scaledBitmap: Bitmap? = null

            val filePath = Objects.requireNonNull<PhotoSelectUtil>(photoSelectUtil).mediaUri?.let {
                UriAdapter.getRealPathFromURI(
                    it, context
                )
            }

            if (filePath == null || filePath.trim { it <= ' ' }.isEmpty())
                return null

            val options = BitmapFactory.Options()

            //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
            //you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(filePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            if (actualHeight == 0 || actualWidth == 0)
                return null

            //max Height and width values of the compressed image is taken as 816x612
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            //width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }

            //setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize =
                calculateInSampleSize(context, options, actualWidth, actualHeight)

            //inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false

            //this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                //load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            try {
                scaledBitmap =
                    Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(Objects.requireNonNull<Bitmap>(scaledBitmap))
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bmp,
                middleX - bmp.width / 2,
                middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
            )

            //check the rotation of the image and display it properly
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val orientation = Objects.requireNonNull<ExifInterface>(exif).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )

            val matrix = Matrix()

            if (orientation == 6)
                matrix.postRotate(90f)
            else if (orientation == 3)
                matrix.postRotate(180f)
            else if (orientation == 8)
                matrix.postRotate(270f)

            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap!!, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix, true
            )

            return scaledBitmap
        }

        fun calculateInSampleSize(
            context: Context,
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            var inSampleSize: Int
            val height = options.outHeight
            val width = options.outWidth
            inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }
    }
}