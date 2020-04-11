package com.uren.motleybrain.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimation(
    internal var view: View,
    internal val targetValue: Int,
    internal var startValue: Int,
    paramType: Int
) : Animation() {
    internal var paramType: Int = 0

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val newValue = (startValue + (targetValue - startValue) * interpolatedTime).toInt()

        if (paramType == widthType)
            view.layoutParams.width = newValue
        else if (paramType == heigthType)
            view.layoutParams.height = newValue

        view.requestLayout()
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

    companion object {

        var widthType = 0
        var heigthType = 1
    }
}