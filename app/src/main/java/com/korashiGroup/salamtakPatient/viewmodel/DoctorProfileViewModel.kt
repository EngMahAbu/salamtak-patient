package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.booking.doctorprofile.DoctorProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DoctorProfileViewModel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val doctorprofileResponse=MutableLiveData<Resource<DoctorProfileResponse>>()


    fun getDoctorProfileByDoctorId(doctorId:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorprofileResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorProfileByDoctorId(doctorId)
        doctorprofileResponse.postValue(response?.let { handlegetHomeCareService(it) })
    }

    private fun handlegetHomeCareService(response: Response<DoctorProfileResponse>): Resource<DoctorProfileResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)

            }
        }
        val error = Gson().fromJson<DoctorProfileResponse>(response.errorBody()!!.string(), DoctorProfileResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}