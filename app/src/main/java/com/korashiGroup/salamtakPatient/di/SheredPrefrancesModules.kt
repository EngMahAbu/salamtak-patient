package com.korashiGroup.salamtakPatient.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SheredPrefrancesModules {

    @Provides
    @Singleton
    fun providesSharedPreferances(context: Application): SharedPreferences {
        return context.getSharedPreferences("db", Context.MODE_PRIVATE)
    }
}