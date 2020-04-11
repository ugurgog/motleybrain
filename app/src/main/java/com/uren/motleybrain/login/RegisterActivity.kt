package com.uren.motleybrain.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.uren.motleybrain.Constants.CustomConstants.LOGIN_USER
import com.uren.motleybrain.Models.LoginUser
import com.uren.motleybrain.Models.User
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.ShapeUtil
import com.uren.motleybrain.utils.dialogBoxUtil.DialogBoxUtil
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.InfoDialogBoxCallback

import java.util.Objects

import io.fabric.sdk.android.Fabric


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var registerLayout: RelativeLayout
    private lateinit var nameET: EditText
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var btnRegister: Button

    //Local
    private lateinit var newLoginUser: LoginUser
    internal lateinit var name: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    internal lateinit var progressDialog: ProgressDialog

    //Firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Fabric.with(this, Crashlytics())
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

        init()
        setShapes()
        //BitmapConversion.setBlurBitmap(RegisterActivity.this, registerLayout,
        //        R.drawable.login_background, 0.2f, 20.5f, null);
    }

    fun setShapes() {
        nameET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        emailET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        passwordET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        btnRegister.background = ShapeUtil.getShape(
            resources.getColor(R.color.colorPrimary, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
    }

    private fun init() {
        registerLayout = findViewById(R.id.registerLayout)
        nameET = findViewById(R.id.input_name)
        emailET = findViewById(R.id.input_email)
        passwordET = findViewById(R.id.input_password)
        btnRegister = findViewById(R.id.btnRegister)
        registerLayout.setOnClickListener(this)
        nameET.setOnClickListener(this)
        emailET.setOnClickListener(this)
        passwordET.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {

        if (v === btnRegister) {
            if (checkNetworkConnection())
                btnRegisterClicked()
        }
    }

    fun checkNetworkConnection(): Boolean {
        if (!CommonUtils.isNetworkConnected(this@RegisterActivity)) {
            CommonUtils.connectionErrSnackbarShow(registerLayout, this@RegisterActivity)
            return false
        } else
            return true
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

    /*****************************CLICK EVENTS */

    private fun btnRegisterClicked() {

        progressDialog.setMessage(this.getString(R.string.REGISTERING_USER))
        progressDialog.show()

        name = nameET.text.toString()
        userEmail = emailET.text.toString()
        userPassword = passwordET.text.toString()

        //validation controls
        if (!checkValidation(name, userEmail, userPassword)) {
            return
        }

        createUser(name, userEmail, userPassword)

    }

    private fun checkValidation(name: String, email: String, password: String): Boolean {

        //username validation
        if (!Validation.getInstance().isValidUserName(this, name)) {
            progressDialog.dismiss()
            Validation.getInstance().errorMessage?.let {
                DialogBoxUtil.showInfoDialogBox(this@RegisterActivity,
                    it, null, object : InfoDialogBoxCallback {
                        override fun okClick() {

                        }
                    })
            }

            return false
        }

        //email validation
        if (!Validation.getInstance().isValidEmail(this, email)) {
            progressDialog.dismiss()
            Validation.getInstance().errorMessage?.let {
                DialogBoxUtil.showInfoDialogBox(this@RegisterActivity,
                    it, null, object : InfoDialogBoxCallback {
                        override fun okClick() {

                        }
                    })
            }
            return false
        }

        //password validation
        if (!Validation.getInstance().isValidPassword(this, password)) {
            //Toast.makeText(this, Validation.getInstance().getErrorMessage() , Toast.LENGTH_SHORT).show();
            progressDialog.dismiss()

            Validation.getInstance().errorMessage?.let {
                DialogBoxUtil.showInfoDialogBox(this@RegisterActivity,
                    it, null, object : InfoDialogBoxCallback {
                        override fun okClick() {

                        }
                    })
            }

            return false
        }

        return true
    }

    private fun createUser(userName: String, userEmail: String, userPassword: String) {

        val context = this

        mAuth!!.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    Log.i("Info", "CreateUser : Success")
                    progressDialog.dismiss()
                    setUserInfo(userEmail)
                    addUserToSystem(userName, userEmail)

                    //startAppIntroPage();
                    //startMainPage();
                } else {
                    progressDialog.dismiss()
                    Log.i("Info", "CreateUser : Fail")
                    try {
                        throw Objects.requireNonNull(task.getException())!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        DialogBoxUtil.showInfoDialogBox(this@RegisterActivity,
                            context.getString(R.string.COLLISION_EXCEPTION),
                            null,
                            object : InfoDialogBoxCallback {
                                override fun okClick() {

                                }
                            })

                    } catch (e: Exception) {
                        DialogBoxUtil.showInfoDialogBox(this@RegisterActivity,
                            context.getString(R.string.UNKNOWN_ERROR) + "(" + e.toString() + ")",
                            null,
                            object : InfoDialogBoxCallback {
                                override fun okClick() {

                                }
                            })
                    }

                }
            }
    }

    private fun addUserToSystem(username: String, email: String) {

        val user = User()
        user.id = mAuth!!.getCurrentUser()?.uid
        user.email = email
        user.isAdmin = false

        startAppIntroPage()


        /*UserDBHelper.addUser(user, new OnCompleteCallback() {
            @Override
            public void OnCompleted() {
                startAppIntroPage();
            }

            @Override
            public void OnFailed(String message) {

            }
        });*/
    }

    private fun setUserInfo(userEmail: String) {

        newLoginUser = LoginUser()
        newLoginUser.email = userEmail
        newLoginUser.userId = Objects.requireNonNull(mAuth!!.getCurrentUser())?.uid
    }

    fun startAppIntroPage() {
        val intent = Intent(this@RegisterActivity, AppIntroductionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(LOGIN_USER, newLoginUser)
        startActivity(intent)
    }
}
