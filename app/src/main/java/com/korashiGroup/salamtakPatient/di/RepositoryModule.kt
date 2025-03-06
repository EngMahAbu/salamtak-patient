package com.korashiGroup.salamtakPatient.di

import android.content.Context
import com.korashiGroup.salamtakPatient.repository.ImageServicesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideImageServicesRepository(@ApplicationContext context: Context): ImageServicesRepository {
        return ImageServicesRepository(context)
    }
}