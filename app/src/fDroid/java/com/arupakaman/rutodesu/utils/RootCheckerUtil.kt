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

    private val sdkVersion = Build.VERSION.SDK_INT

    private val deviceName = Build.MANUFACTURER + " " + Build.MODEL
    fun getDeviceNamePair() = listOf(Pair(mContext.getString(R.string.title_device), deviceName))

    private val fingerPrint = Build.FINGERPRINT

    private val androidVersion = String.format("%s", "Android " + Build.VERSION.RELEASE)

    private val bootLoader = Build.BOOTLOADER

    private val kernelName = System.getProperty("os.name")?:mContext.getString(R.string.msg_unknown)

    private val kernelVersion = System.getProperty("os.version")?:mContext.getString(R.string.msg_unknown)

    private val radioVersion: String
        get() = Build.getRadioVersion()

    private val runtime: String
        get() = if (isArtInUse()) "ART" else "Dalvik"

    private val devMode: String
        get() = if (isDeveloperModeEnabled()) mContext.getString(R.string.msg_on) else mContext.getString(R.string.msg_off)


    private val xposedStatus: String
        get() = if (isAppInstalled("de.robv.android.xposed.installer")) mContext.getString(R.string.msg_installed)
        else mContext.getString(R.string.msg_not_installed)

    private val busyBoxStatus: String
        get() = if (mRootBeer.checkForBusyBoxBinary()) mContext.getString(R.string.msg_installed)
        else mContext.getString(R.string.msg_not_installed)

    private val magiskStatus: String
        get() = if (mRootBeer.checkForMagiskBinary()) mContext.getString(R.string.msg_installed)
        else mContext.getString(R.string.msg_not_installed)

    private val suPath: String
    get() = mRootBeer.pathSuBinary()?:mContext.getString(R.string.str_hiphen)

    private val busyBoxPath: String
        get() = mRootBeer.pathBusyBoxBinary()?:mContext.getString(R.string.str_hiphen)

    private val magiskPath: String
        get() = mRootBeer.pathMagiskBinary()?:mContext.getString(R.string.str_hiphen)

    private val isRootGiven: String
        get() = if (isRootGiven()) mContext.getString(R.string.action_yes) else mContext.getString(R.string.action_no)

    private val testKeys: String
        get() = if (mRootBeer.detectTestKeys()) mContext.getString(R.string.msg_found) else mContext.getString(R.string.msg_not_found)

    private val rwPath: String
        get() = if (mRootBeer.checkForRWPaths()) mContext.getString(R.string.msg_found) else mContext.getString(R.string.msg_not_found)

    private val dangerousProps: String
        get() = if (mRootBeer.checkForDangerousProps()) mContext.getString(R.string.msg_found) else mContext.getString(R.string.msg_not_found)

    private fun rootManagementApps(): String{
        val list = mRootBeer.detectRootManagementApps()
        return if (list.isEmpty()) mContext.getString(R.string.msg_not_found)
        else mContext.getString(R.string.msg_found) + ": \n" + list.joinToString("\n")
    }

    private fun rootCloakingApps(): String{
        val list = mRootBeer.detectRootCloakingApps()
        return if (list.isEmpty()) mContext.getString(R.string.msg_not_found)
        else mContext.getString(R.string.msg_found) + ": \n" + list.joinToString("\n")
    }

    private fun potentiallyUnwantedApps(): String{
        val list = mRootBeer.detectPotentiallyDangerousApps()
        return if (list.isEmpty()) mContext.getString(R.string.msg_not_found)
        else mContext.getString(R.string.msg_found) + ": \n" + list.joinToString("\n")
    }


    fun getTestInfo(): List<Pair<String, String>> = with(mContext){
        return arrayListOf(
                Pair(getString(R.string.title_device), deviceName),
                Pair(getString(R.string.title_android_version), androidVersion),
                Pair(getString(R.string.title_su_path), suPath),
                Pair(getString(R.string.title_busybox), busyBoxStatus),
                Pair(getString(R.string.title_busybox_path), busyBoxPath),
                Pair(getString(R.string.title_magisk), magiskStatus),
                Pair(getString(R.string.title_magisk_path), magiskPath),
                Pair(getString(R.string.title_xposed), xposedStatus),
                Pair(getString(R.string.title_root_given), isRootGiven),
                Pair(getString(R.string.title_test_keys), testKeys),
                Pair(getString(R.string.title_read_write_path), rwPath),
                Pair(getString(R.string.title_dangerous_props), dangerousProps),
                Pair(getString(R.string.title_root_management_apps), rootManagementApps()),
                Pair(getString(R.string.title_root_cloaking_apps), rootCloakingApps()),
                Pair(getString(R.string.title_potentially_unwanted_apps), potentiallyUnwantedApps()),
                Pair(getString(R.string.title_sdk_version), sdkVersion.toString()),
                Pair(getString(R.string.title_kernel), kernelName),
                Pair(getString(R.string.title_kernel_version), kernelVersion),
                Pair(getString(R.string.title_bootloader), bootLoader),
                Pair(getString(R.string.title_runtime), runtime),
                Pair(getString(R.string.title_developer_mode), devMode),
                Pair(getString(R.string.title_fingerprint), fingerPrint),
                Pair(getString(R.string.title_radio_version), radioVersion))
    }



    private fun findBinaryLocation(): String {
        var path = "-"
        for (where in Const.suPaths) {
            if (File(where + "su").exists()) {
                path = where + "su"
                break
            }
        }
        return path
    }

    private fun findBinaryBusyBoxLocation(): String {
        val filename = "busybox"
        for (path in Const.suPaths) {
            val completePath = path + filename
            val f = File(completePath)
            val fileExists = f.exists()
            if (fileExists) {
                return completePath
            }
        }
        return "-"
    }

    private fun isRootGiven(): Boolean {
        if (mRootBeer.isRooted) {
            var process: Process? = null
            try {
                process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
                val input = BufferedReader(InputStreamReader(process?.inputStream))
                val output = input.readLine()
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true
            } catch (e: Exception) {
                Log.e(TAG, "isRootGiven $e")
            } finally {
                process?.destroy()
            }
        }
        return false
    }

    private fun isAppInstalled(pkgName: String): Boolean {
        mContext.runCatching {
            packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
            return true
        }
        return false
    }

    private fun isArtInUse() = System.getProperty("java.vm.version")?.startsWith("2")?:false

    private fun isDeveloperModeEnabled(): Boolean {
        mContext.contentResolver.runCatching {
            return (Settings.Global.getInt(this, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0)
                    || (Settings.Secure.getInt(this, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0)
        }.onFailure {
            Log.e("isDeveloperModeEnabled", "Exc $it")
            @Suppress("DEPRECATION")
            return Settings.Secure.getInt(mContext.contentResolver, Settings.Secure.ADB_ENABLED, 0) == 1
        }
        return false
    }

}