package com.uren.motleybrain.utils


import android.content.Context

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.uren.motleybrain.R

import java.util.Random

object AdMobUtils {

    fun loadBannerAd(adView: AdView) {
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("011E7D1507120248D9EA6F2B4DA8CF4C").build();
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        })
    }

    fun loadInterstitialAd(context: Context) {

        val rand = Random()
        val value = rand.nextInt(10)

        if (value != 2) return

        val mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.setAdUnitId(context.resources.getString(R.string.ADMOB_INTERSITITIAL_AD_UNIT_ID))
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show()
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        })
    }
}
