package com.arupakaman.rutodesu.utils

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdsUtil(private val mActivity: Activity) {

    companion object{
        private val testDevices = listOf("C06D731EC14C89AE801A373A065DD79B")
    }

    init {
        MobileAds.initialize(mActivity.applicationContext)

        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDevices)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)
    }

    fun loadBannerAd(adView: AdView){
        adView.loadAd(AdRequest.Builder().build())
    }

    fun showInterstitialAd(adUnitId: String, onAdLoaded : () -> Unit = {}){
        InterstitialAd.load(mActivity, adUnitId,
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback(){
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    p0.show(mActivity)
                    onAdLoaded()
                }
            })
    }

}