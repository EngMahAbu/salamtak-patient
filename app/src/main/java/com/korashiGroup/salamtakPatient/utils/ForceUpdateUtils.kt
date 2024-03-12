package com.korashiGroup.salamtakPatient.utils

import android.content.Context
import android.content.pm.PackageManager

object ForceUpdateUtils {
    fun getAppVersion(context: Context): String {
        var result = ""
        try {
            result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return result
    }
}