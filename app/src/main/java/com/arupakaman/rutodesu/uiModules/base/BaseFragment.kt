package com.arupakaman.rutodesu.uiModules.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.arupakaman.rutodesu.data.RutodesuSharedPrefs

abstract class BaseFragment : Fragment() {

    protected lateinit var mActivity: Activity

    protected val mPrefs by lazy { RutodesuSharedPrefs.getInstance(mActivity.applicationContext) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

}