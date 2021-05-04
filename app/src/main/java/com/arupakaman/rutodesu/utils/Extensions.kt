package com.arupakaman.rutodesu.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import com.google.android.material.color.MaterialColors

operator fun <T> T.invoke(block: T.() -> Unit) = block()

/**
 *   Toast
 */

fun Context.toast(mMsg: String) {
    Toast.makeText(this, mMsg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes mResId: Int) {
    toast(getString(mResId))
}

fun Context.toastLong(mMsg: String) {
    Toast.makeText(this, mMsg, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(@StringRes mResId: Int) {
    toastLong(getString(mResId))
}

/**
 *  View Properties
 */

var TextView.isUnderlined: Boolean
    get() = ((paintFlags and Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG)
    set(isUnderlined) {
        paintFlags = if(isUnderlined) (paintFlags or Paint.UNDERLINE_TEXT_FLAG) else (paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv())
    }

fun View.disable(mAlpha: Float = 0.4f){
    isEnabled = false
    alpha = mAlpha
}

fun View.enable(){
    isEnabled = true
    alpha = 1.0f
}

var TextView.htmlText: String?
    get() = HtmlCompat.toHtml(
        (text ?: "").toSpanned(),
        HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
    )
    set(string) { text = HtmlCompat.fromHtml(string ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY) }

/**
 *  Resource Functions
 */

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(applicationContext, resId)

fun Context.getColorAttrCompat(@AttrRes resId: Int): Int {
    return MaterialColors.getColor(this, resId, Color.WHITE)
}
