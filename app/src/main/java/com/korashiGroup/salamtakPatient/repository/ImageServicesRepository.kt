package com.korashiGroup.salamtakPatient.repository

import android.content.Context
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.SpUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageServicesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val _imageServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageServicesList: Flow<List<ImageServices>> = _imageServicesList.asStateFlow()
    private val _imageMedicalServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageMedicalServicesList: Flow<List<ImageServices>> =
        _imageMedicalServicesList.asStateFlow()


    fun loadMedicalServices() {
        val list = listOf(
            ImageServices(
                R.drawable.svg_inclinic,
                context.getString(R.string.hospitals),"المستشفيات",
                context.getColor(R.color.color_1),
                2,

            ),
            ImageServices(
                R.drawable.svg_pharmacy,
                context.getString(R.string.pharmacies),"الصيدليات",
                context.getColor(R.color.color_3),
                5,

            ),
            ImageServices(
                R.drawable.svg_labs,
                context.getString(R.string.laboratories),"المختبرات",
                context.getColor(R.color.color_4),
                3,
            ),
            ImageServices(
                R.drawable.svg_radiology,
                context.getString(R.string.radiologycenter),"مراكز الاشعة",
                context.getColor(R.color.color_5),
                4,
            ),
            ImageServices(
                R.drawable.png_salatakangle,
                context.getString(R.string.engile),"سلامتك Angel",
                context.getColor(R.color.angel_color),
                7,
            )
        )
        _imageMedicalServicesList.value = list
    }
}