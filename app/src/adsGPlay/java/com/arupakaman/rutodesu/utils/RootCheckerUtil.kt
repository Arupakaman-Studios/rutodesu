package com.arupakaman.rutodesu.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.arupakaman.rutodesu.R
import com.scottyab.rootbeer.Const
import com.scottyab.rootbeer.RootBeer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class RootCheckerUtil(private val mContext: Context) {

    companion object{

        private val TAG by lazy { "RootCheckerUtil" }

    }

    private val mRootBeer = RootBeer(mContext)

    val isRooted: Boolean
        get() = mRootBeer.checkSuExists() || mRootBeer.checkForRootNative()

    private val deviceName = Build.MANUFACTURER + " " + Build.MODEL
    fun getDeviceNamePair() = listOf(Pair(mContext.getString(R.string.title_device), deviceName))

    private val xposedStatus: String
        get() = if (isAppInstalled("de.robv.android.xposed.installer")) mContext.getString(R.string.msg_installed)
        else mContext.getString(R.string.msg_not_installed)

    private val busyBoxStatus: String
        get() = if (mRootBeer.checkForBusyBoxBinary()) mContext.getString(R.string.msg_installed)
        else mContext.getString(R.string.msg_not_installed)

    fun getTestInfo(): List<Pair<String, String>> = with(mContext){
        return arrayListOf(
                Pair(getString(R.string.title_device), deviceName),
                Pair(getString(R.string.title_busybox), busyBoxStatus),
                Pair(getString(R.string.title_xposed), xposedStatus))
    }

    private fun isAppInstalled(pkgName: String): Boolean {
        mContext.runCatching {
            packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
            return true
        }
        return false
    }

}