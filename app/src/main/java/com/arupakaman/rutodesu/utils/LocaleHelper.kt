package com.arupakaman.rutodesu.utils

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.arupakaman.rutodesu.R
import com.arupakaman.rutodesu.data.RutodesuSharedPrefs
import java.util.Locale

object LocaleHelper {

    fun onAttach(mContext: Context, defaultLanguage: String? = null, defaultLanguageName: String? = null): Context {
        val mPrefs = RutodesuSharedPrefs.getInstance(mContext)
        val lang = mPrefs.selectedLanguageCode?:defaultLanguage?:Locale.ENGLISH.language
        val name = mPrefs.selectedLanguageName?:defaultLanguageName?:Locale.ENGLISH.displayLanguage
        return setLocale(mContext, lang, name)
    }

    //Always use applicationContext for mContext
    fun setLocale(mContext: Context, languageCode: String, languageName: String): Context {
        setPersistedData(mContext, languageCode, languageName)
        return updateResources(mContext, languageCode)
    }

    private fun setPersistedData(mContext: Context, languageCode: String, languageName: String) {
        with(RutodesuSharedPrefs.getInstance(mContext)){
            selectedLanguageCode = languageCode
            selectedLanguageName = languageName
        }
    }

    private fun updateResources(mContext: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        mContext.resources.let { res ->
            val config = Configuration(res.configuration)
            config.setLocale(locale)
            return mContext.createConfigurationContext(config)
        }
    }


    fun getSelectedLanguageCodePosition(mContext: Context): Int{
        kotlin.runCatching {
            val langCodes = mContext.resources.getStringArray(R.array.arr_languages_codes)
            val pos = langCodes.indexOf(RutodesuSharedPrefs.getInstance(mContext).selectedLanguageCode)
            Log.d("getLangCodePosition", "$pos ${langCodes[pos]}")
            return pos
        }.onFailure {
            Log.e("getLangCodePosition", "Get selLangPos Exc : $it")
        }
        return 0
    }

    fun getLanguageByPosition(mContext: Context, pos: Int): Pair<String, String>{
        val langCodes = mContext.resources.getStringArray(R.array.arr_languages_codes)
        val lang = mContext.resources.getStringArray(R.array.arr_languages)
        if (pos >= 0 && pos < lang.size){
            return Pair(langCodes[pos], lang[pos])
        }
        return Pair(langCodes[0], lang[0])
    }

}