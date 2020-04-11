package com.uren.motleybrain.utils

import android.graphics.drawable.GradientDrawable

object ShapeUtil {

    fun getShape(
        backgroundColor: Int,
        borderColor: Int,
        shapeType: Int,
        cornerRadius: Float,
        strokeWidth: Int): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = shapeType
        shape.setColor(backgroundColor)
        if (cornerRadius != 0f)
            shape.cornerRadius = cornerRadius
        if (strokeWidth != 0 && borderColor != 0)
            shape.setStroke(strokeWidth, borderColor)
        return shape
    }

    fun getGradientBackground(startColor: Int, endColor: Int): GradientDrawable {
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )
        gd.cornerRadius = 0f
        return gd
    }

    fun getGradientBackgroundWithMiddleColor(
        startColor: Int,
        middleColor: Int,
        endColor: Int
    ): GradientDrawable {
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, middleColor, endColor)
        )
        gd.cornerRadius = 0f
        return gd
    }
}
