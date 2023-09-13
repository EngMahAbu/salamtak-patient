package com.eltawasol.shipment.courier.sharedpref

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

class SharedPref @Inject constructor(context: Context) {
    @RequiresApi(Build.VERSION_CODES.M)
    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    @RequiresApi(Build.VERSION_CODES.M)
    val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    @RequiresApi(Build.VERSION_CODES.M)
    private var sharedPreferences =
        EncryptedSharedPreferences.create(
            "mySecurleySharedPref",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    @RequiresApi(Build.VERSION_CODES.M)
    fun delete(key: String?) {
        if (sharedPreferences.contains(key)) {
            sharedPreferences.edit()?.remove(key)?.apply()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun savePref(key: String?, value: Any?) {
        delete(key)
        if (value is Boolean) {
            sharedPreferences.edit().putBoolean(key, (value as Boolean?)!!).apply()
        } else if (value is Int) {
            sharedPreferences.edit().putInt(key, (value as Int?)!!).apply()
        } else if (value is Float) {
            sharedPreferences.edit().putFloat(key, (value as Float?)!!).apply()
        } else if (value is Long) {
            sharedPreferences.edit().putLong(key, (value as Long?)!!).apply()
        } else if (value is String) {
            sharedPreferences.edit().putString(key, value as String?).apply()
        } else if (value is Enum<*>) {
            sharedPreferences.edit().putString(key, value.toString()).apply()
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-primitive preference")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun <T> getPref(key: String?, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T?
        return returnValue ?: defValue
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPrefExists(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun reset() {
        sharedPreferences.edit().clear().apply()
    }
}