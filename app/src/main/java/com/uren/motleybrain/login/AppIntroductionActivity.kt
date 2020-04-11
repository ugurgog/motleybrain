package com.uren.motleybrain.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

import com.crashlytics.android.Crashlytics
import com.uren.motleybrain.Constants.CustomConstants.LOGIN_USER
import com.uren.motleybrain.Models.LoginUser
import com.uren.motleybrain.R
import com.uren.motleybrain.login.adapters.AppIntroductionAdapter
import com.uren.motleybrain.management.MainActivity1

import io.fabric.sdk.android.Fabric


class AppIntroductionActivity : AppCompatActivity() {


    private var viewPager: ViewPager? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray? = null
    private var btnSkip: Button? = null
    private var btnNext: Button? = null

    //  viewpager change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts!!.size - 1) {
                    // last page. make button text to GOT IT
                    btnNext!!.text = getString(R.string.GOT_IT)
                    btnSkip!!.visibility = View.GONE
                } else {
                    // still pages are left
                    btnNext!!.text = getString(R.string.next)
                    btnSkip!!.visibility = View.VISIBLE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making notification bar transparent
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(R.layout.activity_app_introduction)
        Fabric.with(this, Crashlytics())
        initVariables()
    }

    private fun initVariables() {
        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)

        layouts =
            intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        val appIntroductionAdapter = AppIntroductionAdapter(this@AppIntroductionActivity, layouts!!)
        viewPager!!.adapter = appIntroductionAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

        btnSkip!!.setOnClickListener { launchHomeScreen() }

        btnNext!!.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts!!.size)
                viewPager!!.currentItem = current
            else
                launchHomeScreen()
        }
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts!!.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.setText(Html.fromHtml("&#8226;"))
            dots[i]?.setTextSize(35f)
            dots[i]?.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots[i])
        }

        if (dots.size > 0)
            dots[currentPage]?.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        //appIntroSession.setFirstTimeLaunch(false);
        val loginUser = intent.getSerializableExtra(LOGIN_USER) as LoginUser

        val intent = Intent(this@AppIntroductionActivity, MainActivity1::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(LOGIN_USER, loginUser)
        startActivity(intent)
        finish()
    }

    private fun changeStatusBarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}
