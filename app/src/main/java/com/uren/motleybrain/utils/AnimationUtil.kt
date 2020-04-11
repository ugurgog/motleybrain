package com.uren.motleybrain.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.uren.motleybrain.R

object AnimationUtil {

    fun setShareItemAnimation(view: View) {
        val set = AnimationSet(true)

        //Animation anim = new ScaleAnimation(1f, 0f, 1f, 0f, 100f, 100f);
        val animT = TranslateAnimation(0f, 100f, 0f, 100f)

        //set.addAnimation(anim);
        set.addAnimation(animT)
        set.duration = 500

        view.startAnimation(set)
    }

    fun blink(context: Context, view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.blink)
        view.startAnimation(animation)
    }
}
