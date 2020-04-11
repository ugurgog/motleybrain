package com.uren.motleybrain.login

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.uren.motleybrain.Constants.CustomConstants.APP_FB_URL
import com.uren.motleybrain.Constants.CustomConstants.APP_PACKAGE_NAME
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.ShapeUtil
import com.uren.motleybrain.utils.dialogBoxUtil.DialogBoxUtil
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.InfoDialogBoxCallback

import java.util.Objects

import io.fabric.sdk.android.Fabric


class ForgetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var forgetPasswordLayout: RelativeLayout
    private lateinit var emailET: EditText
    private lateinit var btnSendLink: Button

    private lateinit var userEmail: String
    internal lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        Fabric.with(this, Crashlytics())
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        init()
        setShapes()
    }

    private fun setShapes() {
        /*   lockImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack, null),
                0, GradientDrawable.OVAL, 50, 0));*/
        emailET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        btnSendLink.background = ShapeUtil.getShape(
            resources.getColor(R.color.colorPrimary,null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 3
        )
    }

    private fun init() {
        emailET = findViewById(R.id.input_email)
        btnSendLink = findViewById(R.id.btnSendLink)
        forgetPasswordLayout = findViewById(R.id.forgetPasswordLayout)
        //lockImgv = findViewById(R.id.lockImgv);
        forgetPasswordLayout.setOnClickListener(this)
        emailET.setOnClickListener(this)
        btnSendLink.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onClick(v: View) {

        if (v === btnSendLink) {
            if (checkNetworkConnection())
                btnSendLinkClicked()
        }
    }

    fun checkNetworkConnection(): Boolean {
        if (!CommonUtils.isNetworkConnected(this@ForgetPasswordActivity)) {
            CommonUtils.connectionErrSnackbarShow(forgetPasswordLayout, this@ForgetPasswordActivity)
            return false
        } else
            return true
    }

    private fun btnSendLinkClicked() {
        progressDialog.setMessage(this.getString(R.string.PLEASE_WAIT))
        progressDialog.show()

        userEmail = emailET.text.toString()

        //validation controls
        if (!checkValidation(userEmail)) {
            return
        }

        sendLinkToMail(userEmail)
    }

    private fun checkValidation(userEmail: String): Boolean {
        if (!Validation.getInstance().isValidEmail(this, userEmail)) {
            progressDialog.dismiss()
            Validation.getInstance().errorMessage?.let {
                DialogBoxUtil.showInfoDialogBox(this@ForgetPasswordActivity,
                    it, null, object : InfoDialogBoxCallback {
                        override fun okClick() {

                        }
                    })
            }
            return false
        }
        return true
    }

    private fun sendLinkToMail(userEmail: String) {

        val context = this

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val actionCodeSettings: ActionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(APP_PACKAGE_NAME, true, null)
            .setHandleCodeInApp(false)
            .setIOSBundleId(null.toString())
            .setUrl(APP_FB_URL)
            .build()

        auth.sendPasswordResetEmail(userEmail, actionCodeSettings)
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    progressDialog.dismiss()
                    if (task.isSuccessful()) {
                        DialogBoxUtil.showInfoDialogBox(this@ForgetPasswordActivity,
                            context.getString(R.string.PASSWORD_LINK_SEND_SUCCESS),
                            null,
                            object : InfoDialogBoxCallback {
                                override fun okClick() {

                                }
                            })
                    } else {
                        DialogBoxUtil.showInfoDialogBox(this@ForgetPasswordActivity,
                            context.getString(R.string.PASSWORD_LINK_SEND_FAIL),
                            null,
                            object : InfoDialogBoxCallback {
                                override fun okClick() {

                                }
                            })
                    }
                }
            })
    }

}
