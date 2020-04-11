package com.uren.motleybrain.utils.dialogBoxUtil

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.GifDialogListener
import kotlinx.android.synthetic.main.layout_gif_dialog_box.*

import java.util.Objects

import pl.droidsonroids.gif.GifImageView


class GifDialogBox private constructor(builder: Builder) {
    internal var gifImageResource: Int = 0

    init {
        val title = builder.title
        val message = builder.message
        val activity = builder.activity
        val pListener = builder.pListener
        val nListener = builder.nListener
        val pBtnColor = builder.pBtnColor
        val nBtnColor = builder.nBtnColor
        val pBtnVisibleType = builder.pBtnVisibleType
        val nBtnVisibleType = builder.nBtnVisibleType
        val titleVisibleType = builder.titleVisibleType
        val positiveBtnText = builder.positiveBtnText
        val negativeBtnText = builder.negativeBtnText
        this.gifImageResource = builder.gifImageResource
        val cancel = builder.cancel
        val durationTime = builder.durationTime
    }

    class Builder(val activity: Activity) {
        var title: String? = null
        var message: String? = null
        var positiveBtnText: String? = null
        var negativeBtnText: String? = null
        var pBtnColor: Int = 0
        var nBtnColor: Int = 0
        var pBtnVisibleType: Int = 0
        var nBtnVisibleType: Int = 0
        var titleVisibleType: Int = 0
        var pListener: GifDialogListener? = null
        var nListener: GifDialogListener? = null
        var cancel: Boolean = false
        var gifImageResource: Int = 0
        var durationTime: Long = 0

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setPositiveBtnText(positiveBtnText: String): Builder {
            this.positiveBtnText = positiveBtnText
            return this
        }

        fun setPositiveBtnBackground(pBtnColor: Int): Builder {
            this.pBtnColor = pBtnColor
            return this
        }

        fun setNegativeBtnText(negativeBtnText: String): Builder {
            this.negativeBtnText = negativeBtnText
            return this
        }

        fun setNegativeBtnBackground(nBtnColor: Int): Builder {
            this.nBtnColor = nBtnColor
            return this
        }

        fun setPositiveBtnVisibility(visibleType: Int): Builder {
            this.pBtnVisibleType = visibleType
            return this
        }

        fun setNegativeBtnVisibility(visibleType: Int): Builder {
            this.nBtnVisibleType = visibleType
            return this
        }

        fun setTitleVisibility(visibleType: Int): Builder {
            this.titleVisibleType = visibleType
            return this
        }

        fun setDurationTime(durationTime: Long): Builder {
            this.durationTime = durationTime
            return this
        }

        fun OnPositiveClicked(pListener: GifDialogListener): Builder {
            this.pListener = pListener
            return this
        }

        fun OnNegativeClicked(nListener: GifDialogListener): Builder {
            this.nListener = nListener
            return this
        }

        fun isCancellable(cancel: Boolean): Builder {
            this.cancel = cancel
            return this
        }

        fun setGifResource(gifImageResource: Int): Builder {
            this.gifImageResource = gifImageResource
            return this
        }

        fun build(): GifDialogBox {
            val dialog = Dialog(this.activity)
            dialog.requestWindowFeature(1)
            Objects.requireNonNull<Window>(dialog.window).setBackgroundDrawable(ColorDrawable(0))
            dialog.setCancelable(this.cancel)
            dialog.setContentView(R.layout.layout_gif_dialog_box)
            val title1 = dialog.title
            val message1 = dialog.message
            val nBtn = dialog.negativeBtn
            val pBtn = dialog.positiveBtn
            val gifImageView = dialog.gifImageView

            gifImageView.setImageResource(this.gifImageResource)
            nBtn.setVisibility(nBtnVisibleType)
            pBtn.setVisibility(pBtnVisibleType)
            title1.setText(this.title)
            message1.setText(this.message)

            if (this.positiveBtnText != null) {
                pBtn.setText(this.positiveBtnText)
            }

            if (this.negativeBtnText != null) {
                nBtn.setText(this.negativeBtnText)
            }

            if (this.pListener != null) {
                pBtn.setOnClickListener(View.OnClickListener {
                    this@Builder.pListener!!.OnClick()
                    dialog.dismiss()
                })
            } else {
                pBtn.setOnClickListener(View.OnClickListener { dialog.dismiss() })
            }

            if (this.nListener != null) {
                nBtn.setVisibility(View.VISIBLE)
                nBtn.setOnClickListener(View.OnClickListener {
                    this@Builder.nListener!!.OnClick()
                    dialog.dismiss()
                })
            }

            try {
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (this.durationTime > 0) {
                Handler().postDelayed({
                    if (dialog.isShowing)
                        dialog.dismiss()
                }, this.durationTime)
            }
            return GifDialogBox(this)
        }
    }
}
