package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.MedicalExaminationResponse
import com.korashiGroup.salamtakPatient.repository.ImageServicesRepository
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.DateUtils.context
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ImageServicesViewModel @Inject constructor(
    private val repository1: ImageServicesRepository,
    private val repository2: RegisterRepositry
) : ViewModel() {

    val imageServicesList = MutableLiveData<Resource<List<ImageServices>>>()

    private val _imageMedicalServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageMedicalServicesList: StateFlow<List<ImageServices>> = _imageMedicalServicesList.asStateFlow()

    init {
        fetchMedicalServices()
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception", "osama${exception.message.toString()}")
        imageServicesList.postValue(Resource.sucess(getDefaultServicesList()))
    }

    fun fetchImageServices() = viewModelScope.launch(Dispatchers.IO + handler) {
        imageServicesList.postValue(Resource.Loading())
        try {
            val response = repository2.loadImageServices()
            if (response != null && response.isSuccessful) {
                imageServicesList.postValue(handleImageServicesResponse(response))
            } else {
                imageServicesList.postValue(Resource.sucess(getDefaultServicesList()))
            }
        } catch (e: Exception) {
            imageServicesList.postValue(Resource.sucess(getDefaultServicesList()))
        }
    }

    private fun getDefaultServicesList(): List<ImageServices> {
        return listOf(
            ImageServices(R.drawable.m_book, "In Clinic Booking", "حجز عيادة", Id = 1),
            ImageServices(R.drawable.m_home, "Home Visit", "زيارة منزلية", Id = 2),
            ImageServices(R.drawable.polyclinic_image_new, "PolyClinics", "مراكز طبية", Id = 6),
            ImageServices(R.drawable.m_video, "Video Call", "مكالمة فيديو", Id = 8),
            ImageServices(R.drawable.m_call, "Call", "استشارة هاتفية", Id = 9),
            ImageServices(R.drawable.offers_image_new, context.getString(R.string.offers), "العروض", Id = 7),
            ImageServices(R.drawable.ic_hospital_service, context.getString(R.string.hospitals), "مستشفيات", Id = 11)
        )
    }

    private fun handleImageServicesResponse(it: Response<MedicalExaminationResponse>): Resource<List<ImageServices>> {
        if (it.isSuccessful) {
            it.body()?.let { responseBody ->

                // 1. Map API MedicalExamination items into ImageServices objects
                val apiMappedList = responseBody.data?.filterNotNull()?.map { examination ->
                    ImageServices(
                        image_onboarding = 0,
                        textService = examination.name ?: "",
                        textServiceArabic = examination.name ?: "",
                        color = 0,
                        Id = examination.id,
                        image = examination.image
                    )
                } ?: emptyList()

                // 2. Find specific API items to arrange in order matching screenshot:
                // Row 1: In Clinic Booking | Home Visit | PolyClinics
                // Row 2: Video Call | Call | Offers
                // Row 3: Hospitals (Centered)
                val inClinicItem = apiMappedList.find { it.Id == 1 }
                    ?: apiMappedList.find { it.textService.contains("Clinic", ignoreCase = true) || it.textServiceArabic?.contains("عيادة") == true }
                val homeVisitItem = apiMappedList.find { it.Id == 2 || it.Id == 10 }
                    ?: apiMappedList.find { it.textService.contains("Home", ignoreCase = true) || it.textServiceArabic?.contains("منزل") == true }
                val videoCallItem = apiMappedList.find { it.Id == 8 || it.Id == 3 || it.Id == 4 }
                    ?: apiMappedList.find { it.textService.contains("Video", ignoreCase = true) || it.textServiceArabic?.contains("فيديو") == true }
                val callItem = apiMappedList.find { it.Id == 9 || it.Id == 5 }
                    ?: apiMappedList.find { it.textService.contains("Call", ignoreCase = true) || it.textServiceArabic?.contains("مكالمة") == true || it.textServiceArabic?.contains("هاتف") == true }

                val itemInClinic = inClinicItem?.copy(
                    image_onboarding = R.drawable.m_book,
                    textService = "In Clinic Booking",
                    textServiceArabic = "حجز عيادة",
                    image = null
                ) ?: ImageServices(R.drawable.m_book, "In Clinic Booking", "حجز عيادة", Id = 1)

                val itemHomeVisit = homeVisitItem?.copy(
                    image_onboarding = R.drawable.m_home,
                    textService = "Home Visit",
                    textServiceArabic = "زيارة منزلية",
                    image = null
                ) ?: ImageServices(R.drawable.m_home, "Home Visit", "زيارة منزلية", Id = 2)

                val itemPolyClinic = ImageServices(
                    R.drawable.polyclinic_image_new,
                    "PolyClinics",
                    "مراكز طبية",
                    Id = 6
                )

                val itemVideoCall = videoCallItem?.copy(
                    image_onboarding = R.drawable.m_video,
                    textService = "Video Call",
                    textServiceArabic = "مكالمة فيديو",
                    image = null
                ) ?: ImageServices(R.drawable.m_video, "Video Call", "مكالمة فيديو", Id = 8)

                val itemCall = callItem?.copy(
                    image_onboarding = R.drawable.m_call,
                    textService = "Call",
                    textServiceArabic = "استشارة هاتفية",
                    image = null
                ) ?: ImageServices(R.drawable.m_call, "Call", "استشارة هاتفية", Id = 9)

                val itemOffers = ImageServices(
                    R.drawable.offers_image_new,
                    context.getString(R.string.offers),
                    "العروض",
                    Id = 7
                )
                val itemHospitals = ImageServices(
                    R.drawable.ic_hospital_service,
                    context.getString(R.string.hospitals),
                    "مستشفيات",
                    Id = 11
                )

                val combinedList = listOf(
                    itemInClinic,
                    itemHomeVisit,
                    itemPolyClinic,
                    itemVideoCall,
                    itemCall,
                    itemOffers,
                    itemHospitals
                )

                return Resource.sucess(combinedList)
            }
        }

        return Resource.sucess(getDefaultServicesList())
    }

    private fun fetchMedicalServices() {
        viewModelScope.launch {
            repository1.imageMedicalServicesList.collect { list ->
                _imageMedicalServicesList.value = list
            }
        }
        repository1.loadMedicalServices()
    }
}
