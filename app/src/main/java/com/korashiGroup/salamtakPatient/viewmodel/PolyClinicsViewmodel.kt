package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicResponse
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsDoctorsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PolyClinicsViewmodel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val polyClinicsResponse=MutableLiveData<Resource<PolyClinicResponse>>()
    val polyClinicsDoctorsResponse=MutableLiveData<Resource<PolyClinicsDoctorsResponse>>()
    val videosDoctorsResponse=MutableLiveData<Resource<PolyClinicsDoctorsResponse>>()


    fun getFilteredHealthEntity(polyclinicRequest: HashMap<String, String> )=viewModelScope.launch(Dispatchers.IO+handler) {
        polyClinicsResponse.postValue(Resource.Loading())
        val response=repositry.getFilteredHealthEntity(polyclinicRequest)
        polyClinicsResponse.postValue(response?.let { handlecreateDoctorRate(it) })
    }

    private fun handlecreateDoctorRate(response: Response<PolyClinicResponse>): Resource<PolyClinicResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PolyClinicResponse>(response.errorBody()!!.string(), PolyClinicResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getDoctorByClinicId(ClinicId :Int,SpecID :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        polyClinicsDoctorsResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorByClinicId(ClinicId,SpecID)
        polyClinicsDoctorsResponse.postValue(response?.let { handlegetDoctorByClinicId(it) })
    }

    private fun handlegetDoctorByClinicId(response: Response<PolyClinicsDoctorsResponse>): Resource<PolyClinicsDoctorsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PolyClinicsDoctorsResponse>(response.errorBody()!!.string(), PolyClinicsDoctorsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getDoctorVideos(doctorId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        videosDoctorsResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorVideos(doctorId)
        videosDoctorsResponse.postValue(response?.let { handlegetDoctorVideos(it) })
    }

    private fun handlegetDoctorVideos(response: Response<PolyClinicsDoctorsResponse>): Resource<PolyClinicsDoctorsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PolyClinicsDoctorsResponse>(response.errorBody()!!.string(), PolyClinicsDoctorsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}