package com.korashiGroup.salamtakPatient.utils.localization

import android.content.Context

interface LocaleHelperKt {

    fun onAttach(defaultLanguage: String? = null): Context
    fun getCurrentLocale(): String
    fun setCurrentLocale(language: String): Context
}