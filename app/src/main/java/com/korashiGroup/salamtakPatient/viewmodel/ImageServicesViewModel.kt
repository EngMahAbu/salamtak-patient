package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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

    // Changed to Resource<List<ImageServices>> so both API items and static items fit together
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

                // 1. Map API MedicalExamination items into ImageServices objects
                val apiMappedList = responseBody.data?.filterNotNull()?.map { examination ->
                    ImageServices(
                        image_onboarding = 0, // Set to 0 since we are using a remote URL string
                        textService = examination.name ?: "",
                        textServiceArabic = examination.name ?: "", // Fallback or use Arabic field if added later
                        color = 0,
                        Id = examination.id,
                        image = examination.image // Pass the image URL string here from the API
                    )
                } ?: emptyList()

                // 2. Create your static items (using string literals to avoid Context leaks)
                val offersItem = ImageServices(
                     R.drawable.ic_offers,context.getString(R.string.offers),
                    "العروض",
                    Id = 7
                )

                val polyclinicItem = ImageServices(
                  R.drawable.ic_polyclinics,
                    context.getString(R.string.polyclinics),"مراكز طبية",
                    Id = 6
                )

                // 3. Combine API items and static items into a single mutable list
                val combinedList = apiMappedList.toMutableList().apply {
                    add(polyclinicItem)
                    add(offersItem)
                }

                return Resource.sucess(combinedList)
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

    private fun fetchMedicalServices() {
        viewModelScope.launch {
            repository1.imageMedicalServicesList.collect { list ->
                _imageMedicalServicesList.value = list
            }
        }
        repository1.loadMedicalServices()
    }
}