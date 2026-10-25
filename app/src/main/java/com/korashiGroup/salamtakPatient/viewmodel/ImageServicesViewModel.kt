package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.MedicalExaminationResponse
import com.korashiGroup.salamtakPatient.repository.ImageServicesRepository
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
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
    }

    fun fetchImageServices() = viewModelScope.launch(Dispatchers.IO + handler) {
        imageServicesList.postValue(Resource.Loading())
        val response = repository2.loadImageServices()
        if (response != null) {
            imageServicesList.postValue(handleImageServicesResponse(response))
        } else {
            imageServicesList.postValue(Resource.Error("Failed to fetch data"))
        }
    }

    private fun handleImageServicesResponse(it: Response<MedicalExaminationResponse>): Resource<List<ImageServices>> {
        if (it.isSuccessful) {
            it.body()?.let { responseBody ->
                val sortedList = responseBody.data
                    ?.filterNotNull()
                    ?.filter { examination -> examination.inactive != true }
                    ?.distinctBy { examination -> examination.id }
                    ?.map { examination ->
                        ImageServices(
                            image_onboarding = 0,
                            textService = examination.name ?: "",
                            textServiceArabic = examination.name ?: "",
                            color = 0,
                            Id = examination.id,
                            image = examination.image
                        )
                    }
                    ?.sortedBy { getServiceCategory(it) }
                    ?: emptyList()

                return Resource.sucess(sortedList)
            }
        }

        val errorBody = it.errorBody()?.string()
        val errorMessage = if (!errorBody.isNullOrEmpty()) {
            try {
                val error = Gson().fromJson(errorBody, MedicalExaminationResponse::class.java)
                it.message()
            } catch (e: Exception) {
                it.message()
            }
        } else {
            it.message()
        }

        return Resource.Error(errorMessage ?: "Unknown Error")
    }

    private fun getServiceCategory(service: ImageServices): Int {
        val id = service.Id
        val nameEn = service.textService.lowercase()
        val nameAr = service.textServiceArabic?.lowercase() ?: ""

        return when {
            // 1. In Clinic Booking
            (nameEn.contains("clinic") && !nameEn.contains("poly")) || (nameAr.contains("عياد") && !nameAr.contains("مراكز") && !nameAr.contains("مركز")) || id == 1 -> 1

            // 2. Home Visit
            nameEn.contains("home") || nameAr.contains("منزل") || id == 2 || id == 10 -> 2

            // 3. PolyClinic
            nameEn.contains("poly") || nameAr.contains("مراكز") || nameAr.contains("مركز") || nameAr.contains("بولي") || id == 6 -> 3

            // 4. Video Call
            nameEn.contains("video") || nameAr.contains("فيديو") || id == 8 -> 4

            // 5. Call (voice call only)
            ((nameEn.contains("call") || nameAr.contains("صوت") || nameAr.contains("مكالمة") || nameAr.contains("هاتف"))
                    && !nameEn.contains("video") && !nameAr.contains("فيديو")) || id == 9 || id == 5 -> 5

            // 6. Offers
            nameEn.contains("offer") || nameAr.contains("عروض") || nameAr.contains("عرض") || id == 7 -> 6

            else -> 100 + (id ?: 0)
        }
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