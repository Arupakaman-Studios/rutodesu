package com.arupakaman.rutodesu.uiModules.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arupakaman.rutodesu.BuildConfig
import com.arupakaman.rutodesu.R
import com.arupakaman.rutodesu.databinding.ActivityHomeBinding
import com.arupakaman.rutodesu.uiModules.about.FragmentAbout
import com.arupakaman.rutodesu.uiModules.base.BaseAppCompatActivity
import com.arupakaman.rutodesu.uiModules.openAppInPlayStore
import com.arupakaman.rutodesu.uiModules.openContactMail
import com.arupakaman.rutodesu.uiModules.openShareAppIntent
import com.arupakaman.rutodesu.uiModules.settings.FragmentSettings
import com.arupakaman.rutodesu.utils.*

class ActivityHome : BaseAppCompatActivity() {

    companion object {

        private val TAG by lazy { "ActivityHome" }

    }

    private val mBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var mAdapterDrawer: AdapterHomeDrawer
    private lateinit var mAdapterTestInfo: AdapterTestInfo

    private lateinit var mRootChecker: RootCheckerUtil
    private lateinit var mAdsUtil: AdsUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding{

            mRootChecker = RootCheckerUtil(applicationContext)
            mAdsUtil = AdsUtil(this@ActivityHome)

            mAdapterDrawer = AdapterHomeDrawer(ArrayList(resources.getStringArray(R.array.arr_home_drawer)
                    .toList()).apply {
                if (BuildConfig.isAdsOn) add(getString(R.string.title_rutodesu_pro)) }){
                actRootView.transitionToStart()
                fragmentNav(it)
            }

            rvHomeDrawer.adapter = mAdapterDrawer
            if (supportFragmentManager.backStackEntryCount > 0){
                mAdapterDrawer.setSelected(1)
            }

            fun toggleDrawer(){
                actRootView{
                    if (currentState == R.id.end) transitionToStart()
                    else transitionToEnd()
                }
            }

            ivDrawerDotsLhs.setSafeOnClickListener { toggleDrawer() }
            ivDrawerDotsRhs.setSafeOnClickListener { toggleDrawer() }

            includeFragmentHome{
                ivRutoArtHome.setOptimizedBitmap(R.drawable.img_ruto_art)

                mAdapterTestInfo = AdapterTestInfo()
                rvTestInfo.adapter = mAdapterTestInfo

                mAdapterTestInfo.submit(mRootChecker.getDeviceNamePair())
                tvResultValue.text = getString(R.string.title_unchecked)
                tvMoreInfo.isVisible = false
                tvMoreInfo.isUnderlined = true

                fun checkRoot(){
                    progressTest.isVisible = true
                    tvResultValue {
                        if (mRootChecker.isRooted) {
                            text = getString(R.string.title_rooted)
                            setTextColor(getColorCompat(R.color.colorGreen))
                        } else {
                            text = getString(R.string.title_not_rooted)
                            setTextColor(getColorCompat(R.color.colorReiRed))
                        }
                    }
                    mAdapterTestInfo.submit(mRootChecker.getTestInfo())
                    progressTest.isVisible = false
                    tvMoreInfo.isVisible = BuildConfig.isAdsOn
                    btnCheckHome.text = getString(R.string.action_check_again)
                    btnCheckHome{
                        setBackgroundColor(getColorAttrCompat(R.attr.colorPrimary))
                        setTextColor(getColorAttrCompat(R.attr.colorTextMain))
                        strokeColor = ColorStateList.valueOf(getColorAttrCompat(R.attr.colorTextMain))
                        setStrokeWidthResource(R.dimen.btn_stroke_width)
                    }
                }
                btnCheckHome.setSafeOnClickListener {
                    if (BuildConfig.isAdsOn){
                        val count = mPrefs.checkRootCount
                        if (count == 3) {
                            mAdsUtil.showInterstitialAd(getString(R.string.key_ad_mob_interstitial_id)) {
                                mPrefs.checkRootCount = 0
                            }
                        }else {
                            mPrefs.checkRootCount = count + 1
                            checkRoot()
                        }
                    }else checkRoot()
                }

                tvMoreInfo.setSafeOnClickListener {
                    openAppInPlayStore(getString(R.string.pro_package_name))
                }

                mAdsUtil.loadBannerAd(includeAd.adView)

            }

        }
    }

    private var lastBackPressed = 0L

    override fun onBackPressed() {
        when{
            (mBinding.actRootView.currentState == R.id.end) -> {
                mBinding.actRootView.transitionToStart()
            }
            (supportFragmentManager.backStackEntryCount > 0) ->{
                mAdapterDrawer.setSelected()
                super.onBackPressed()
            }
            ((lastBackPressed + 1000) < System.currentTimeMillis()) -> {
                lastBackPressed = System.currentTimeMillis()
                toast(R.string.msg_home_back_press_again_to_exit)
            }
            else -> super.onBackPressed()
        }
    }


    private fun fragmentNav(pos: Int){
        fun commitFrag(frag: Fragment, tag: String){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContainerHome, frag, tag)
                    .addToBackStack(tag)
                    .commit()
            supportFragmentManager.executePendingTransactions()
        }
        if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        when(pos){
            0 -> mAdapterDrawer.setSelected(pos)
            1 -> {
                commitFrag(FragmentSettings(), "Settings")
                mAdapterDrawer.setSelected(pos)
            }
            2 -> {
                commitFrag(FragmentAbout(), "About")
                mAdapterDrawer.setSelected(pos)
            }
            3 -> openContactMail()
            4 -> openShareAppIntent()
            5 -> AppReviewUtil.askForReview(this)
            6 -> openAppInPlayStore(getString(R.string.pro_package_name))
        }
    }


}