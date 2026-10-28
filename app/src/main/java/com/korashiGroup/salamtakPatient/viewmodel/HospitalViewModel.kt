package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.hospital.HospitalDoctorResponse
import com.korashiGroup.salamtakPatient.model.hospital.HospitalResponse
import com.korashiGroup.salamtakPatient.repository.HospitalRepository
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val repository: HospitalRepository
) : ViewModel() {

    val hospitalsResponse = MutableLiveData<Resource<HospitalResponse>>()
    val hospitalDoctorsResponse = MutableLiveData<Resource<HospitalDoctorResponse>>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("HospitalViewModel", "Error: ${exception.message}")
        hospitalsResponse.postValue(Resource.Error(exception.message ?: "An unexpected error occurred"))
        hospitalDoctorsResponse.postValue(Resource.Error(exception.message ?: "An unexpected error occurred"))
    }

    fun fetchHospitals(
        cityId: Int?,
        areaId: Int?,
        specialistId: Int?,
        searchByName: String? = null
    ) = viewModelScope.launch(Dispatchers.IO + handler) {
        hospitalsResponse.postValue(Resource.Loading())
        val response = repository.getAllHospitals(cityId, areaId, specialistId, searchByName)
        if (response != null) {
            hospitalsResponse.postValue(handleHospitalResponse(response))
        } else {
            hospitalsResponse.postValue(Resource.Error("Failed to fetch hospitals"))
        }
    }

    private fun handleHospitalResponse(response: Response<HospitalResponse>): Resource<HospitalResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val errorBody = response.errorBody()?.string()
        val errorMsg = if (!errorBody.isNullOrEmpty()) {
            try {
                val error = Gson().fromJson(errorBody, HospitalResponse::class.java)
                error.message ?: response.message()
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        return Resource.Error(errorMsg ?: "Unknown error")
    }

    fun fetchHospitalDoctors(
        hospitalId: Int?,
        specialistId: Int?,
        searchByDoctorName: String? = null
    ) = viewModelScope.launch(Dispatchers.IO + handler) {
        hospitalDoctorsResponse.postValue(Resource.Loading())
        val response = repository.getHospitalDoctors(hospitalId, specialistId, searchByDoctorName)
        if (response != null) {
            hospitalDoctorsResponse.postValue(handleHospitalDoctorResponse(response))
        } else {
            hospitalDoctorsResponse.postValue(Resource.Error("Failed to fetch hospital doctors"))
        }
    }

    private fun handleHospitalDoctorResponse(response: Response<HospitalDoctorResponse>): Resource<HospitalDoctorResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val errorBody = response.errorBody()?.string()
        val errorMsg = if (!errorBody.isNullOrEmpty()) {
            try {
                val error = Gson().fromJson(errorBody, HospitalDoctorResponse::class.java)
                error.message ?: response.message()
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        return Resource.Error(errorMsg ?: "Unknown error")
    }
}
