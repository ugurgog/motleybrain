package com.uren.motleybrain.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView

import com.crashlytics.android.Crashlytics
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.uren.motleybrain.Constants.CustomConstants.LOGIN_USER
import com.uren.motleybrain.Models.LoginUser
import com.uren.motleybrain.R
import com.uren.motleybrain.management.MainActivity1
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.ShapeUtil

import java.util.Objects

import io.fabric.sdk.android.Fabric

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var backgroundLayout: RelativeLayout
    internal lateinit var emailET: EditText
    internal lateinit var passwordET: EditText
    internal lateinit var registerText: AppCompatTextView
    internal lateinit var forgetPasText: AppCompatTextView
    internal lateinit var btnLogin: Button
    internal lateinit var forgetPasswordBtn: Button
    internal lateinit var createAccBtn: Button
    internal lateinit var llGoogleSignIn: LinearLayout
    private var rememberMeCheckBox: CheckBox? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null

    //Local
    internal lateinit var userEmail: String
    internal lateinit var userPassword: String
    internal lateinit var progressDialog: ProgressDialog
    lateinit var loginUser: LoginUser

    //Firebase
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setContentView(R.layout.activity_login)
        Fabric.with(this, Crashlytics())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        initVariables()
        setShapes()
    }

    fun setShapes() {
        emailET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        passwordET.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        btnLogin.background = ShapeUtil.getShape(
            resources.getColor(R.color.colorPrimary, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        llGoogleSignIn.background = ShapeUtil.getShape(
            resources.getColor(R.color.GoogleLogin, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        forgetPasswordBtn.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
        createAccBtn.background = ShapeUtil.getShape(
            resources.getColor(R.color.transparent, null),
            resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 20F, 4
        )
    }

    @SuppressLint("CommitPrefEdits")
    private fun initVariables() {
        initUIValues()
        setClickableTexts(this)
        initUIListeners()
        progressDialog = ProgressDialog(this)

        loginUser = LoginUser()
        mAuth = FirebaseAuth.getInstance()

        val loginPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        loginPrefsEditor = loginPreferences.edit()

        val saveLogin = loginPreferences.getBoolean("saveLogin", false)
        if (saveLogin) {
            emailET.setText(loginPreferences.getString("email", emailET.text.toString()))
            passwordET.setText(loginPreferences.getString("password", passwordET.text.toString()))
            rememberMeCheckBox!!.isChecked = true
        }
    }

    private fun initUIValues() {
        backgroundLayout = findViewById(R.id.loginLayout)
        emailET = findViewById(R.id.input_email)
        passwordET = findViewById(R.id.input_password)
        registerText = findViewById(R.id.btnRegister)
        forgetPasText = findViewById(R.id.btnForgetPassword)
        btnLogin = findViewById(R.id.btnLogin)
        rememberMeCheckBox = findViewById(R.id.rememberMeCb)
        forgetPasswordBtn = findViewById(R.id.forgetPasswordBtn)
        createAccBtn = findViewById(R.id.createAccBtn)
        llGoogleSignIn = findViewById(R.id.llGoogleSignIn)
    }

    private fun initUIListeners() {
        backgroundLayout.setOnClickListener(this)
        emailET.setOnClickListener(this)
        passwordET.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        llGoogleSignIn.setOnClickListener(this)
    }

    private fun setClickableTexts(act: Activity) {
        val textRegister = resources.getString(R.string.createAccount)
        val textForgetPssword = resources.getString(R.string.forgetPassword)
        val spanStringRegister = SpannableString(textRegister)
        val spanStringForgetPas = SpannableString(textForgetPssword)
        spanStringRegister.setSpan(UnderlineSpan(), 0, spanStringRegister.length, 0)
        spanStringForgetPas.setSpan(UnderlineSpan(), 0, spanStringForgetPas.length, 0)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {

                if (textView == registerText) {
                    //Toast.makeText(LoginActivity.this, "RegisterActivity click!", Toast.LENGTH_SHORT).show();
                    registerTextClicked()
                } else if (textView == forgetPasText) {
                    //Toast.makeText(LoginActivity.this, "Forgetpas click!", Toast.LENGTH_SHORT).show();
                    forgetPasTextClicked()
                }
            }
        }
        spanStringRegister.setSpan(clickableSpan, 0, spanStringRegister.length, 0)
        spanStringForgetPas.setSpan(clickableSpan, 0, spanStringForgetPas.length, 0)

        registerText.text = spanStringRegister
        forgetPasText.text = spanStringForgetPas
        registerText.movementMethod = LinkMovementMethod.getInstance()
        forgetPasText.movementMethod = LinkMovementMethod.getInstance()
        registerText.highlightColor = Color.TRANSPARENT
        forgetPasText.highlightColor = Color.TRANSPARENT
        registerText.setLinkTextColor(resources.getColor(R.color.White, null))
        forgetPasText.setLinkTextColor(resources.getColor(R.color.White, null))
    }

    override fun onClick(view: View) {

        if (view === backgroundLayout) {
            saveLoginInformation()
            CommonUtils.hideKeyBoard(this@LoginActivity)
        } else if (view === btnLogin) {
            if (checkNetworkConnection())
                loginBtnClicked()
        } else if (view === rememberMeCheckBox) {
            saveLoginInformation()
        } else if (view === llGoogleSignIn) {
            googleSignIn()
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun checkNetworkConnection(): Boolean {
        if (!CommonUtils.isNetworkConnected(this@LoginActivity)) {
            CommonUtils.connectionErrSnackbarShow(backgroundLayout, this@LoginActivity)
            return false
        } else
            return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveLoginInformation()

        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            saveLoginInformation()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun saveLoginInformation() {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Objects.requireNonNull(imm).hideSoftInputFromWindow(emailET.windowToken, 0)

        val username = emailET.text.toString()
        val password = passwordET.text.toString()

        if (rememberMeCheckBox!!.isChecked) {
            loginPrefsEditor!!.putBoolean("saveLogin", true)
            loginPrefsEditor!!.putString("email", username)
            loginPrefsEditor!!.putString("password", password)
            loginPrefsEditor!!.commit()
        } else {
            loginPrefsEditor!!.clear()
            loginPrefsEditor!!.commit()
        }
    }

    private fun registerTextClicked() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        //finish();
    }

    private fun forgetPasTextClicked() {
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun loginBtnClicked() {

        saveLoginInformation()
        progressDialog.setMessage(this.getString(R.string.LOGGING_USER))
        progressDialog.show()

        userEmail = emailET.text.toString()
        userPassword = passwordET.text.toString()

        //validation controls
        if (!checkValidation(userEmail, userPassword)) {
            return
        }

        loginUser(userEmail, userPassword)
    }

    private fun checkValidation(email: String, password: String): Boolean {

        //email validation
        if (!Validation.getInstance().isValidEmail(this, email)) {
            //Toast.makeText(this, Validation.getInstance().getErrorMessage() , Toast.LENGTH_SHORT).show();
            progressDialog.dismiss()
            openDialog(Validation.getInstance().errorMessage)
            return false
        }

        //password validation
        if (!Validation.getInstance().isValidPassword(this, password)) {
            //Toast.makeText(this, Validation.getInstance().getErrorMessage() , Toast.LENGTH_SHORT).show();
            progressDialog.dismiss()
            openDialog(Validation.getInstance().errorMessage)
            return false
        }

        return true
    }

    fun openDialog(message: String?) {

        val alert = AlertDialog.Builder(this)
        alert.setTitle("OOPS!!")
        alert.setMessage(message)
        alert.setPositiveButton("OK", null)
        alert.show()

    }

    private fun loginUser(userEmail: String, userPassword: String) {
        val context = this

        mAuth!!.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this
            ) { task ->
                progressDialog.dismiss()

                if (task.isSuccessful()) {
                    setUserInfo(userEmail)
                    startMainPage()
                } else {

                    try {
                        throw Objects.requireNonNull(task.getException())!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.i("error register", e.toString())
                        openDialog(context.getString(R.string.INVALID_CREDENTIALS))
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Log.i("error register", e.toString())
                        openDialog(context.getString(R.string.INVALID_USER))
                    } catch (e: Exception) {
                        Log.i("error signIn ", e.toString())
                        openDialog(context.getString(R.string.UNKNOWN_ERROR) + "(" + e.toString() + ")")

                    }

                }
            }
    }

    private fun setUserInfo(userEmail: String) {
        loginUser.email = userEmail
        loginUser.userId = Objects.requireNonNull(mAuth!!.currentUser)?.uid
    }

    private fun startMainPage() {
        loginUser.userId = Objects.requireNonNull(mAuth!!.currentUser)?.uid
        val intent = Intent(this, MainActivity1::class.java)
        intent.putExtra(LOGIN_USER, loginUser)
        startActivity(intent)
        finish()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                CommonUtils.showToastShort(
                    this@LoginActivity,
                    resources.getString(R.string.googleSignInFailed)
                )
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override  fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        val user = mAuth!!.getCurrentUser()

                        /*UserDBHelper.getUser(user.getUid(), new CompleteCallback() {
                                @Override
                                public void onComplete(Object object) {
                                    if(object == null || ((User) object).getUserid() == null){
                                        String username = UserDataUtil.getUsernameFromNameWhenLoginWithGoogle(user.getDisplayName());
                                        User newUser = new User(user.getUid(), user.getDisplayName(), username,
                                                user.getEmail(), user.getPhotoUrl().toString(), null, null, false, LOGIN_METHOD_GOOGLE);

                                        UserDBHelper.addUser(newUser, new OnCompleteCallback() {
                                            @Override
                                            public void OnCompleted() {
                                                setUserInfo(username, user.getEmail());
                                                startAppIntroPage();
                                            }

                                            @Override
                                            public void OnFailed(String message) {
                                                CommonUtils.showToastShort(LoginActivity.this, message);
                                            }
                                        });
                                    }else {
                                        User user1 = (User) object;
                                        user1.setLoginMethod(LOGIN_METHOD_GOOGLE);

                                        UserDBHelper.updateUser(user1, false, new OnCompleteCallback() {
                                            @Override
                                            public void OnCompleted() {
                                                setUserInfo("", user.getEmail());
                                                startMainPage();
                                            }

                                            @Override
                                            public void OnFailed(String message) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailed(String message) {
                                    CommonUtils.showToastShort(LoginActivity.this, message);
                                }
                            });*/

                    } else {
                        CommonUtils.showToastShort(
                            this@LoginActivity,
                            task.getException().toString()
                        )
                    }
                }
            })
    }

    fun startAppIntroPage() {
        val intent = Intent(this@LoginActivity, AppIntroductionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(LOGIN_USER, loginUser)
        startActivity(intent)
    }

    companion object {

        private val RC_SIGN_IN = 9001
    }
}
