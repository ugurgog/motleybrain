package com.uren.motleybrain.utils.ClickableImage

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class EffectTouchListener : View.OnTouchListener {

    internal lateinit var rect: Rect // Variable rect to hold the bounds of the view

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (view is ImageView) {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    rect = Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())
                    view.setColorFilter(FILTERED_GREY, PorterDuff.Mode.SRC_ATOP)
                    view.invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    // if move inside button do nothing, otherwise clear filter
                    if (rect.contains(
                            view.getLeft() + event.x.toInt(),
                            view.getTop() + event.y.toInt()
                        )
                    )
                    view.clearColorFilter()
                    view.invalidate()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL // Action call when button is inside scrollable view
                -> {
                    view.clearColorFilter()
                    view.invalidate()
                }
            }
        }
        return false
    }

    companion object {

        private val FILTERED_GREY = Color.argb(125, 0, 0, 0)
    }

}
