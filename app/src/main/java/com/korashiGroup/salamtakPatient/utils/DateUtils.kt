package com.korashiGroup.salamtakPatient.utils

import android.content.Context
import com.dro.doctoronline_doctorapp.localDataBase.SharedPreferencesTool
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    lateinit var context: Context
    fun setup(context: Context) {
        this.context = context
    }

    fun convertLongToDate(time:Long):String{
        val date= Date(time)
        val sdf= SimpleDateFormat("dd:MM:yyyy", Locale.US)
        return sdf.format(date)
    }

    fun Long.toTimeDateString(): String {
        val dateTime = Date(this)
        val format = SimpleDateFormat(" yyyy/MM/dd", Locale.US)
        return format.format(dateTime)
    }
    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd")
        return df.parse(date).time
    }

    fun setCurrentTime(CurrentTime: String){
        SharedPreferencesTool.setString(context, SharedPreferencesKeys.CurrentTime.name, CurrentTime)
    }
    fun getCurrentTime(): String? {
        return SharedPreferencesTool.getString(context,SharedPreferencesKeys.CurrentTime.name)
    }

    fun getToken(): String? {
        return SharedPreferencesTool.getString(context,SharedPreferencesKeys.PREF_USER_TOKEN.name)
    }

    fun setToken(token: String) {
        SharedPreferencesTool.setString(context, SharedPreferencesKeys.PREF_USER_TOKEN.name, token)
    }
    fun setLanguage(lang: String) {
        SharedPreferencesTool.setString(context, SharedPreferencesKeys.Language.name, lang)
    }
    fun getLanguage(): String? {
        return SharedPreferencesTool.getString(context,SharedPreferencesKeys.Language.name)
    }

    fun setLanguageFromMore(lang: String) {
        SharedPreferencesTool.setString(context, SharedPreferencesKeys.LanguageMore.name, lang)
    }
    fun getLanguageFromMore(): String? {
        return SharedPreferencesTool.getString(context,SharedPreferencesKeys.LanguageMore.name)
    }

    fun getFcmToken(): String? {
        return SharedPreferencesTool.getString(context,SharedPreferencesKeys.PREF_USER_FCM_TOKEN.name)
    }

    fun setFcmToken(token: String) {
        SharedPreferencesTool.setString(context, SharedPreferencesKeys.PREF_USER_FCM_TOKEN.name, token)
    }
}