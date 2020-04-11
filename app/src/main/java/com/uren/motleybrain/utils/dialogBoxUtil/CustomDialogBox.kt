package com.uren.motleybrain.utils.dialogBoxUtil


import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.uren.motleybrain.Models.User
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.dataModelUtil.UserDataUtil
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.CustomDialogListener
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.CustomDialogReturnListener
import kotlinx.android.synthetic.main.layout_custom_dialog_box.*

import java.util.Objects

class CustomDialogBox private constructor(builder: Builder) {

    init {
        val title = builder.title
        val message = builder.message
        val edittextMsg = builder.edittextMsg
        val activity = builder.activity
        val pListener = builder.pListener
        val nListener = builder.nListener
        val returnListener = builder.returnListener
        val pBtnColor = builder.pBtnColor
        val nBtnColor = builder.nBtnColor
        val pBtnVisibleType = builder.pBtnVisibleType
        val nBtnVisibleType = builder.nBtnVisibleType
        val editTextVisibleType = builder.editTextVisibleType
        val positiveBtnText = builder.positiveBtnText
        val negativeBtnText = builder.negativeBtnText
        val user = builder.user
        val cancel = builder.cancel
        val durationTime = builder.durationTime
    }

    class Builder(val activity: Activity) {
        var title: String? = null
        var message: String? = null
        var positiveBtnText: String? = null
        var negativeBtnText: String? = null
        var edittextMsg: String? = null
        var pBtnColor: Int = 0
        var nBtnColor: Int = 0
        var pBtnVisibleType: Int = 0
        var nBtnVisibleType: Int = 0
        var editTextVisibleType: Int = 0
        var pListener: CustomDialogListener? = null
        var nListener: CustomDialogListener? = null
        var returnListener: CustomDialogReturnListener? = null
        var cancel: Boolean = false
        var user: User? = null
        var durationTime: Long = 0

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setEditTextVisibility(visibleType: Int): Builder {
            this.editTextVisibleType = visibleType
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setEditTextMessage(edittextMsg: String): Builder {
            this.edittextMsg = edittextMsg
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


        fun setDurationTime(durationTime: Long): Builder {
            this.durationTime = durationTime
            return this
        }

        fun OnPositiveClicked(pListener: CustomDialogListener): Builder {
            this.pListener = pListener
            return this
        }

        fun OnNegativeClicked(nListener: CustomDialogListener): Builder {
            this.nListener = nListener
            return this
        }

        fun OnReturnListenerSet(listener: CustomDialogReturnListener): Builder {
            this.returnListener = listener
            return this
        }

        fun isCancellable(cancel: Boolean): Builder {
            this.cancel = cancel
            return this
        }

        fun setUser(user: User): Builder {
            this.user = user
            return this
        }

        fun build(): CustomDialogBox {
            val dialog = Dialog(this.activity)
            dialog.requestWindowFeature(1)
            Objects.requireNonNull<Window>(dialog.window).setBackgroundDrawable(ColorDrawable(0))
            dialog.setCancelable(this.cancel)
            dialog.setContentView(R.layout.layout_custom_dialog_box)
            val title1 = dialog.title
            val message1 = dialog.message
            val shortUserNameTv = dialog.shortUserNameTv
            val profilePicImgView = dialog.profilePicImgView
            val usernameTextView = dialog.usernameTextView
            val nBtn = dialog.negativeBtn
            val pBtn = dialog.positiveBtn
            val relativelayout1 = dialog.relativelayout1
            val editText = dialog.editText
            val buttonsView = dialog.buttonsView

            nBtn.setVisibility(nBtnVisibleType)
            pBtn.setVisibility(pBtnVisibleType)
            editText.setVisibility(editTextVisibleType)

            if (nBtnVisibleType == View.GONE || pBtnVisibleType == View.GONE)
                buttonsView.setVisibility(View.GONE)

            if (message != null && !message!!.isEmpty())
                message1.setText(this.message)
            else
                message1.setVisibility(View.GONE)

            if (edittextMsg != null && !edittextMsg!!.isEmpty())
                editText.setText(edittextMsg)

            if (title != null && !title!!.isEmpty())
                title1.setText(this.title)
            else
                title1.setVisibility(View.GONE)

            if (user != null) {
                UserDataUtil.setProfilePicture(
                    this.activity, user!!.profilePhotoUrl,
                    user!!.name, shortUserNameTv, profilePicImgView, false
                )
                UserDataUtil.setNameOrUserName(user!!.name, usernameTextView)
            } else
                relativelayout1.setVisibility(View.GONE)

            if (pBtnColor != 0) {
                val bgShape = pBtn.getBackground() as GradientDrawable
                bgShape.setColor(pBtnColor)
            }
            if (nBtnColor != 0) {
                val bgShape = nBtn.getBackground() as GradientDrawable
                bgShape.setColor(nBtnColor)
            }

            if (this.positiveBtnText != null) {
                pBtn.setText(this.positiveBtnText)
            }

            if (this.negativeBtnText != null) {
                nBtn.setText(this.negativeBtnText)
            }

            if (this.pListener != null) {
                pBtn.setOnClickListener({ view ->

                    if (this@Builder.returnListener != null && editText.getText() != null && !editText.getText().toString().isEmpty()) {
                        this@Builder.returnListener!!.OnReturn(editText.getText().toString())
                        dialog.dismiss()
                    } else {
                        this@Builder.pListener!!.OnClick()
                        dialog.dismiss()
                    }
                })
            } else {
                pBtn.setOnClickListener({ view -> dialog.dismiss() })
            }

            if (this.nListener != null) {
                nBtn.setVisibility(View.VISIBLE)
                nBtn.setOnClickListener({ view ->
                    this@Builder.nListener!!.OnClick()
                    dialog.dismiss()
                })
            }

            dialog.show()

            if (this.durationTime > 0) {
                Handler().postDelayed({
                    if (dialog.isShowing)
                        dialog.dismiss()
                }, this.durationTime)
            }
            return CustomDialogBox(this)
        }
    }
}
