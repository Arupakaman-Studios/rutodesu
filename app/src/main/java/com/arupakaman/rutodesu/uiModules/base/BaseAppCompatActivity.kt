package com.arupakaman.rutodesu.uiModules.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arupakaman.rutodesu.R
import com.arupakaman.rutodesu.data.RutodesuSharedPrefs
import com.arupakaman.rutodesu.uiModules.settings.FragmentSettings
import com.arupakaman.rutodesu.utils.LocaleHelper

abstract class BaseAppCompatActivity : AppCompatActivity() {

    protected val mPrefs by lazy { RutodesuSharedPrefs.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        FragmentSettings.updateTheme(mPrefs.selectedThemeMode)
        when(mPrefs.selectedThemeMode){
            3 -> setTheme(R.style.Theme_Rutodesu_Purple)
            4 -> setTheme(R.style.Theme_Rutodesu_Rei)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
            window.navigationBarColor = Color.BLACK
        }
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

}