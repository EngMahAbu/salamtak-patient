package com.korashiGroup.salamtakPatient.repository

import android.content.Context
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.model.ImageServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageServicesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _imageServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageServicesList: Flow<List<ImageServices>> = _imageServicesList.asStateFlow()
    private val _imageMedicalServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageMedicalServicesList: Flow<List<ImageServices>> =
        _imageMedicalServicesList.asStateFlow()

    fun loadImageServices() {
        val list = listOf(
            ImageServices(
                R.drawable.ic_inclinic,
                context.getString(R.string.Clinic_Booking),"كشف بالعيادة",
                Id = 1,
            ),
            ImageServices(R.drawable.ic_home_visit, context.getString(R.string.Home_Visit),"كشف منزلى", Id = 2),
            ImageServices(
                R.drawable.ic_polyclinics,
                context.getString(R.string.polyclinics),"مراكز طبية",
                Id = 6
            ),
            ImageServices(R.drawable.ic_vedieocall, context.getString(R.string.Video_Call),"مكالمة فيديو", Id = 3),
            ImageServices(R.drawable.ic_calls, context.getString(R.string.Call),"مكالمة صوتية", Id = 4),
            ImageServices(R.drawable.ic_offers, context.getString(R.string.offers),"العروض", Id = 7),
            ImageServices(R.drawable.ic_labs, context.getString(R.string.labs),"التحاليل", Id = 8),
            ImageServices(
                R.drawable.ic_ray_radiology,
                context.getString(R.string.medicalscan),"الأشعة",
                Id = 9,
            ),
            ImageServices(R.drawable.ic_homecares, context.getString(R.string.home_care),"الرعاية المنزلية", Id = 10)
        )
        _imageServicesList.value = list
    }

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
//            ImageServices(
//                R.drawable.svg_labs,
//                context.getString(R.string.laboratories),"المختبرات",
//                context.getColor(R.color.color_4),
//                3,
//            ),
//            ImageServices(
//                R.drawable.svg_radiology,
//                context.getString(R.string.radiologycenter),"مراكز الاشعة",
//                context.getColor(R.color.color_5),
//                4,
//            ),
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