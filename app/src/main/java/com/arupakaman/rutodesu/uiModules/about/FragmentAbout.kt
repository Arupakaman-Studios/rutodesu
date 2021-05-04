package com.arupakaman.rutodesu.uiModules.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arupakaman.rutodesu.R
import com.arupakaman.rutodesu.databinding.FragmentAboutBinding
import com.arupakaman.rutodesu.uiModules.base.BaseFragment
import com.arupakaman.rutodesu.uiModules.openContactMail
import com.arupakaman.rutodesu.uiModules.openShareAppIntent
import com.arupakaman.rutodesu.uiModules.settings.FragmentSettings
import com.arupakaman.rutodesu.utils.AppReviewUtil
import com.arupakaman.rutodesu.utils.invoke
import com.arupakaman.rutodesu.utils.isUnderlined
import com.arupakaman.rutodesu.utils.setSafeOnClickListener

class FragmentAbout : BaseFragment(){

    companion object{
        private val TAG by lazy { "FragmentAbout" }
    }

    private val mBinding by lazy { FragmentAboutBinding.inflate(mActivity.layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding {

            tvAboutApp.movementMethod = LinkMovementMethod.getInstance()

            tvContact.isUnderlined = true
            tvShare.isUnderlined = true
            tvRate.isUnderlined = true
            setViewListeners()

        }
    }

    private fun FragmentAboutBinding.setViewListeners(){
        tvContact.setSafeOnClickListener {
            mActivity.openContactMail()
        }

        tvShare.setSafeOnClickListener {
            mActivity.openShareAppIntent()
        }

        tvRate.setSafeOnClickListener {
            AppReviewUtil.askForReview(mActivity)
        }
    }

}