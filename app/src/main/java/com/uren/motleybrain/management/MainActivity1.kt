package com.uren.motleybrain.management

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.uren.motleybrain.Models.User
import com.uren.motleybrain.R
import com.uren.motleybrain.login.LoginActivity
import com.uren.motleybrain.utils.AnimationUtil
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.ShapeUtil
import io.fabric.sdk.android.Fabric

class MainActivity1 : Activity(){

    private lateinit var mainActLayout: RelativeLayout
    private lateinit var appIconImgv: ImageView
    private lateinit var refresh_layout: SwipeRefreshLayout
    private lateinit var tryAgainButton: Button
    private lateinit var networkTryDesc: TextView

    private var firebaseAuth: FirebaseAuth? = null
    internal var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())
        CommonUtils.hideKeyBoard(this)
        initVariables()


        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth!!.getCurrentUser() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else
            fillUserInfo()
    }

    private fun initVariables() {
        mainActLayout = findViewById(R.id.mainActLayout)
        refresh_layout = findViewById(R.id.refresh_layout)
        appIconImgv = findViewById(R.id.appIconImgv)
        tryAgainButton = findViewById(R.id.tryAgainButton)
        networkTryDesc = findViewById(R.id.networkTryDesc)
        AnimationUtil.blink(this@MainActivity1, appIconImgv)

        tryAgainButton.background = ShapeUtil.getShape(
            this@MainActivity1.resources.getColor(R.color.DodgerBlue, null),
            this@MainActivity1.resources.getColor(R.color.White, null), GradientDrawable.RECTANGLE, 50F, 2
        )

        setPullToRefresh()
        addListeners()
    }

    private fun addListeners() {
        tryAgainButton.setOnClickListener { loginProcess() }
    }

    private fun setPullToRefresh() {
        refresh_layout.setOnRefreshListener { loginProcess() }
    }

    fun fillUserInfo() {
        loginProcess()
    }

    fun loginProcess() {
        if (!CommonUtils.isNetworkConnected(this@MainActivity1)) {
            tryAgainButton.visibility = View.VISIBLE
            networkTryDesc.visibility = View.VISIBLE
            CommonUtils.connectionErrSnackbarShow(mainActLayout, this@MainActivity1)
            refresh_layout.isRefreshing = false
        } else {
            tryAgainButton.visibility = View.GONE
            networkTryDesc.visibility = View.GONE
            startLoginProcess()
        }
    }

    fun startLoginProcess() {

        startActivity(Intent(this@MainActivity1, NextActivity::class.java))

        /*UserDBHelper.getUser(firebaseAuth.getCurrentUser().getUid(), new CompleteCallback() {
            @Override
            public void onComplete(Object object) {
                user = (User) object;

                if (user != null) {
                    EventBus.getDefault().postSticky(new UserBus(user));
                    refresh_layout.setRefreshing(false);
                    updateDeviceTokenForFCM();
                    startActivity(new Intent(MainActivity.this, NextActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailed(String message) {
                CommonUtils.showToastShort(MainActivity.this, message);
                refresh_layout.setRefreshing(false);
            }
        });*/
    }

    fun updateDeviceTokenForFCM() {
        /*TokenDBHelper.updateTokenSigninValue(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), CHAR_E,
                new OnCompleteCallback() {
                    @Override
                    public void OnCompleted() {
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String deviceToken = instanceIdResult.getToken();
                                TokenDBHelper.sendTokenToServer(deviceToken, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                            }
                        });
                    }

                    @Override
                    public void OnFailed(String message) {

                    }
                });*/
    }
}
