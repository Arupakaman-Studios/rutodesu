package com.arupakaman.rutodesu.utils

import com.arupakaman.rutodesu.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.reportExceptionToCrashlytics(msg: String){
    if (!BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().log("MANUAL_LOGGING : $msg | Exc : $this")
}