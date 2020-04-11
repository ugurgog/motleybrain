package com.uren.motleybrain.management

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import java.util.Objects

//import butterknife.ButterKnife
import com.uren.motleybrain.Constants.CustomConstants.ANIMATE_DOWN_TO_UP
import com.uren.motleybrain.Constants.CustomConstants.ANIMATE_LEFT_TO_RIGHT
import com.uren.motleybrain.Constants.CustomConstants.ANIMATE_RIGHT_TO_LEFT
import com.uren.motleybrain.Constants.CustomConstants.ANIMATE_UP_TO_DOWN
import com.uren.motleybrain.R
import com.uren.motleybrain.evetBusModels.UserBus
import com.uren.motleybrain.game.GameFragment
import com.uren.motleybrain.management.fragmentControllers.FragNavController
import com.uren.motleybrain.management.fragmentControllers.FragNavController.Companion.TAB1
import com.uren.motleybrain.management.fragmentControllers.FragNavController.Companion.TAB2
import com.uren.motleybrain.management.fragmentControllers.FragNavController.Companion.TAB3
import com.uren.motleybrain.management.fragmentControllers.FragNavTransactionOptions
import com.uren.motleybrain.management.fragmentControllers.FragmentHistory
import com.uren.motleybrain.profile.ProfileFragment
import com.uren.motleybrain.statistics.StatisticsFragment
import com.uren.motleybrain.utils.AdMobUtils
import io.fabric.sdk.android.Fabric


class NextActivity : FragmentActivity(), BaseFragment.FragmentNavigation,
    FragNavController.TransactionListener, FragNavController.RootFragmentListener {


    lateinit var thisActivity: Activity

    lateinit var contentFrame: FrameLayout
    lateinit var profilePageMainLayout: LinearLayout
    lateinit var bottomTabLayout: TabLayout

    lateinit var tabMainLayout: LinearLayout

    private var linearLayoutOne: LinearLayout? = null
    private var linearLayout2: LinearLayout? = null
    private var linearLayout3: LinearLayout? = null

    private var imgv1: ImageView? = null
    private var imgv2: ImageView? = null
    private var imgv3: ImageView? = null

    var ANIMATION_TAG: String? = null

    var transactionOptions: FragNavTransactionOptions? = null

    lateinit var TABS: Array<String>

    private var mNavController: FragNavController? = null

    private var fragmentHistory: FragmentHistory? = null

    lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        Fabric.with(this, Crashlytics())
        thisActivity = this

        initValues()

        fragmentHistory = FragmentHistory()

        mNavController = FragNavController.newBuilder(
            savedInstanceState,
            supportFragmentManager,
            R.id.content_frame
        )
            .transactionListener(this)
            .rootFragmentListener(this, TABS.size)
            .build()

        switchTab(0)
        updateTabSelection(0)

        bottomTabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabSelectionControl(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                mNavController!!.clearStack()
                tabSelectionControl(tab)
            }
        })
    }

    fun tabSelectionControl(tab: TabLayout.Tab) {
        fragmentHistory!!.push(tab.position)
        switchAndUpdateTabSelection(tab.position)
    }

    private fun initValues() {
        //ButterKnife.bind(this)
        bottomTabLayout = findViewById(R.id.tablayout)
        profilePageMainLayout = findViewById(R.id.profilePageMainLayout)
        contentFrame = findViewById(R.id.content_frame)
        tabMainLayout = findViewById(R.id.tabMainLayout)
        adView = findViewById(R.id.adView)
        TABS = resources.getStringArray(R.array.tab_name)

        //setStatusBarTransparent();
        initTab()
        MobileAds.initialize(this@NextActivity, resources.getString(R.string.ADMOB_APP_ID))
        AdMobUtils.loadBannerAd(adView)
    }

    private fun initTab() {
        if (bottomTabLayout != null) {
            for (i in TABS.indices) {
                bottomTabLayout!!.addTab(bottomTabLayout!!.newTab())
                //TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
            }
        }

        @SuppressLint("InflateParams") val headerView =
            (Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE)) as LayoutInflater)
                .inflate(R.layout.custom_tab, null, false)

        linearLayoutOne = headerView.findViewById(R.id.ll)
        linearLayout2 = headerView.findViewById(R.id.ll2)
        linearLayout3 = headerView.findViewById(R.id.ll3)

        imgv1 = headerView.findViewById(R.id.imgv1)
        imgv2 = headerView.findViewById(R.id.imgv2)
        imgv3 = headerView.findViewById(R.id.imgv3)

        Objects.requireNonNull<TabLayout.Tab>(bottomTabLayout!!.getTabAt(0)).setCustomView(linearLayoutOne)
        Objects.requireNonNull<TabLayout.Tab>(bottomTabLayout!!.getTabAt(1)).setCustomView(linearLayout2)
        Objects.requireNonNull<TabLayout.Tab>(bottomTabLayout!!.getTabAt(2)).setCustomView(linearLayout3)
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true)
    fun customEventReceived(userBus: UserBus) {
        val user = userBus.user
    }

    fun switchTab(position: Int) {
        mNavController!!.switchTab(position)
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {

        if (!mNavController!!.isRootFragment) {
            setTransactionOption()
            mNavController!!.popFragment(transactionOptions)
        } else {

            if (fragmentHistory!!.isEmpty) {
                super.onBackPressed()
            } else {

                if (fragmentHistory!!.stackSize > 1) {

                    val position = fragmentHistory!!.popPrevious()
                    switchAndUpdateTabSelection(position)
                } else {
                    switchAndUpdateTabSelection(0)
                    fragmentHistory!!.emptyStack()
                }
            }
        }
    }

    fun switchAndUpdateTabSelection(position: Int) {
        switchTab(position)
        updateTabSelection(position)
    }

    private fun setTransactionOption() {
        if (transactionOptions == null) {
            transactionOptions = FragNavTransactionOptions.newBuilder().build()
        }

        if (ANIMATION_TAG != null) {
            when (ANIMATION_TAG) {
                ANIMATE_RIGHT_TO_LEFT -> {
                    transactionOptions!!.enterAnimation = R.anim.slide_from_right
                    transactionOptions!!.exitAnimation = R.anim.slide_to_left
                    transactionOptions!!.popEnterAnimation = R.anim.slide_from_left
                    transactionOptions!!.popExitAnimation = R.anim.slide_to_right
                }
                ANIMATE_LEFT_TO_RIGHT -> {
                    transactionOptions!!.enterAnimation = R.anim.slide_from_left
                    transactionOptions!!.exitAnimation = R.anim.slide_to_right
                    transactionOptions!!.popEnterAnimation = R.anim.slide_from_right
                    transactionOptions!!.popExitAnimation = R.anim.slide_to_left
                }
                ANIMATE_DOWN_TO_UP -> {
                    transactionOptions!!.enterAnimation = R.anim.slide_from_down
                    transactionOptions!!.exitAnimation = R.anim.slide_to_up
                    transactionOptions!!.popEnterAnimation = R.anim.slide_from_up
                    transactionOptions!!.popExitAnimation = R.anim.slide_to_down
                }
                ANIMATE_UP_TO_DOWN -> {
                    transactionOptions!!.enterAnimation = R.anim.slide_from_up
                    transactionOptions!!.exitAnimation = R.anim.slide_to_down
                    transactionOptions!!.popEnterAnimation = R.anim.slide_from_down
                    transactionOptions!!.popExitAnimation = R.anim.slide_to_up
                }
                else -> transactionOptions = null
            }
        } else
            transactionOptions = null
    }

    fun updateTabSelection(currentTab: Int) {

        for (i in TABS.indices) {
            val selectedTab = bottomTabLayout!!.getTabAt(i)
            val drawable = selectedTab!!.customView!!.background as GradientDrawable

            if (currentTab != i)
                drawable.setColor(resources.getColor(R.color.colorPrimaryDark))
            else {
                selectedTab.customView!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@NextActivity,
                        R.anim.tab_anim
                    )
                )

                when (i) {
                    TAB1 -> {
                        drawable.setColor(resources.getColor(R.color.gplus_color_2))
                        imgv1!!.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@NextActivity,
                                R.anim.tab_rotate_anim
                            )
                        )
                    }
                    TAB2 -> {
                        drawable.setColor(resources.getColor(R.color.gplus_color_3))
                        imgv2!!.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@NextActivity,
                                R.anim.tab_rotate_anim
                            )
                        )
                    }
                    TAB3 -> {
                        drawable.setColor(resources.getColor(R.color.gplus_color_4))
                        imgv3!!.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@NextActivity,
                                R.anim.tab_rotate_anim
                            )
                        )
                    }
                    else -> drawable.setColor(resources.getColor(R.color.colorPrimaryDark))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mNavController != null) {
            mNavController!!.onSaveInstanceState(outState)
        }
    }

    override fun pushFragment(fragment: Fragment) {
        if (mNavController != null) {
            mNavController!!.pushFragment(fragment)
        }
    }

    override fun pushFragment(fragment: Fragment, animationTag: String) {

        ANIMATION_TAG = animationTag
        setTransactionOption()

        if (mNavController != null) {
            mNavController!!.pushFragment(fragment, transactionOptions)
        }
    }

    override fun getRootFragment(index: Int): Fragment {
        when (index) {

            TAB1 -> return GameFragment()
            TAB2 -> return StatisticsFragment()
            TAB3 -> return ProfileFragment()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {

    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {

    }

}