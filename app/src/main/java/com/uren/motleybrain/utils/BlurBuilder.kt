package com.uren.motleybrain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

object BlurBuilder {

    fun blur(v: View, bitmapScale: Float, blurRadius: Float): Bitmap {
        return blur(v.context, getScreenshot(v, v.context), bitmapScale, blurRadius)
    }

    fun blur(ctx: Context, image: Bitmap, bitmapScale: Float, blurRadius: Float): Bitmap {
        val outputBitmap: Bitmap

        val width = Math.round(image.width * bitmapScale)
        val height = Math.round(image.height * bitmapScale)

        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(ctx)
        val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(blurRadius)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }

    private fun getScreenshot(v: View, context: Context): Bitmap {
        val b: Bitmap
        b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }
}