package com.arupakaman.rutodesu.uiModules.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.arupakaman.rutodesu.R
import com.arupakaman.rutodesu.data.RutodesuSharedPrefs
import com.arupakaman.rutodesu.databinding.FragmentSettingsBinding
import com.arupakaman.rutodesu.uiModules.base.BaseFragment
import com.arupakaman.rutodesu.utils.LocaleHelper
import com.arupakaman.rutodesu.utils.getColorCompat
import com.arupakaman.rutodesu.utils.invoke
import com.arupakaman.rutodesu.utils.isUnderlined
import com.arupakaman.rutodesu.utils.setSafeOnClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FragmentSettings : BaseFragment(){

    companion object{
        private val TAG by lazy { "FragmentSettings" }

        fun updateTheme(theme: Int){
            when(theme){
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

    }

    private val mBinding by lazy { FragmentSettingsBinding.inflate(mActivity.layoutInflater) }
    var mDialog: Dialog? = null

    private var selLangPos = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding {

            selLangPos = LocaleHelper.getSelectedLanguageCodePosition(mActivity)

            updateLanguageUi()
            checkSelectedTheme(mPrefs.selectedThemeMode)
            setThemesView()
            setViewListeners()

        }
    }

    private fun FragmentSettingsBinding.setViewListeners(){
        fun onThemeSelect(theme: Int){
            mPrefs.selectedThemeMode = theme
            updateTheme(theme)
            mActivity.recreate()
        }

        viewThemeSystem.setSafeOnClickListener {
            onThemeSelect(0)
            checkSelectedTheme(0)
        }

        viewThemeLight.setSafeOnClickListener {
            onThemeSelect(1)
            checkSelectedTheme(1)
        }

        viewThemeDark.setSafeOnClickListener {
            onThemeSelect(2)
            checkSelectedTheme(2)
        }

        viewThemePurple.setSafeOnClickListener {
            onThemeSelect(3)
            checkSelectedTheme(3)
        }

        viewThemeRei.setSafeOnClickListener {
            onThemeSelect(4)
            checkSelectedTheme(4)
        }

        clLanguage.setSafeOnClickListener {
            showLanguageSelectDialog()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun FragmentSettingsBinding.updateLanguageUi(){
        tvLanguageDesc.text = getString(R.string.desc_current_language_colon) + " " + LocaleHelper.getLanguageByPosition(mActivity, selLangPos).second
    }

    private fun showLanguageSelectDialog(){
        mDialog = MaterialAlertDialogBuilder(mActivity)
            .setTitle(getString(R.string.title_select_language_colon))
            .setSingleChoiceItems(R.array.arr_languages, LocaleHelper.getSelectedLanguageCodePosition(mActivity)){ _, which ->
                selLangPos = which
            }
            .setPositiveButton(R.string.action_select){ dialog, _ ->
                val selLang = LocaleHelper.getLanguageByPosition(mActivity, selLangPos)
                LocaleHelper.setLocale(mActivity, selLang.first, selLang.second)
                dialog.dismiss()
                mActivity.recreate()
            }
            .setNegativeButton(R.string.action_cancel){ dialog, _ ->
                dialog.dismiss()
            }.create()

        mDialog?.show()
    }

    private fun FragmentSettingsBinding.checkSelectedTheme(theme: Int){
        tvThemeSystem.isUnderlined = theme == 0
        tvThemeLight.isUnderlined = theme == 1
        tvThemeDark.isUnderlined = theme == 2
        tvThemePurple.isUnderlined = theme == 3
        tvThemeRei.isUnderlined = theme == 4
    }

    private fun FragmentSettingsBinding.setThemesView(){
        (viewThemeLight.background as GradientDrawable?)?.apply {
            setColor(mActivity.getColorCompat(R.color.colorLight))
            setStroke(3, mActivity.getColorCompat(R.color.colorLightX))
        }
        (viewThemeDark.background as GradientDrawable?)?.apply {
            setColor(mActivity.getColorCompat(R.color.colorDark))
            setStroke(3, mActivity.getColorCompat(R.color.colorDarkX))
        }
        (viewThemePurple.background as GradientDrawable?)?.apply {
            setColor(mActivity.getColorCompat(R.color.colorLightVar))
            setStroke(3, mActivity.getColorCompat(R.color.colorLightX))
        }
        (viewThemeRei.background as GradientDrawable?)?.apply {
            setColor(mActivity.getColorCompat(R.color.colorRei))
            setStroke(3, mActivity.getColorCompat(R.color.colorLightX))
        }
    }

}