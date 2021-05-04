package com.arupakaman.rutodesu

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.arupakaman.rutodesu.data.RutodesuSharedPrefs
import com.arupakaman.rutodesu.uiModules.settings.FragmentSettings
import com.arupakaman.rutodesu.utils.DefaultExceptionHandler
import com.arupakaman.rutodesu.utils.LocaleHelper

class RutodesuApp : MultiDexApplication() {

    companion object{

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Thread.setDefaultUncaughtExceptionHandler(DefaultExceptionHandler(this))

        //Init
        RutodesuSharedPrefs.getInstance(this)

        /*val theme = mPrefs.selectedThemeMode
        if (theme < 3) FragmentSettings.updateTheme(theme)*/

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.onAttach(this)
    }

}