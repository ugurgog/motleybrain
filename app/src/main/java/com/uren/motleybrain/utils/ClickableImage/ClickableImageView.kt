package com.uren.motleybrain.utils.ClickableImage

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView

class ClickableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    init {
        setOnTouchListener(EffectTouchListener())
    }
}
