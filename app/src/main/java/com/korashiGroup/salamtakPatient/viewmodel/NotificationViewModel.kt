package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.notification.DoctorNotificationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val doctorResponse=MutableLiveData<Resource<DoctorNotificationResponse>>()
    val doctorMarkResponse=MutableLiveData<Resource<DoctorNotificationResponse>>()


    fun getUnreadDoctorResponses()=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorResponse.postValue(Resource.Loading())
        val response=repositry.getUnreadDoctorResponses()
        doctorResponse.postValue(response?.let { handleGetUnreadDoctorResponses(it) })
    }

    private fun handleGetUnreadDoctorResponses(response: Response<DoctorNotificationResponse>): Resource<DoctorNotificationResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<DoctorNotificationResponse>(response.errorBody()!!.string(), DoctorNotificationResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun markAllDoctorResponses()=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorMarkResponse.postValue(Resource.Loading())
        val response=repositry.markAllDoctorResponses()
        doctorMarkResponse.postValue(response?.let { handleMarkAllDoctorResponses(it) })
    }

    private fun handleMarkAllDoctorResponses(response: Response<DoctorNotificationResponse>): Resource<DoctorNotificationResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<DoctorNotificationResponse>(response.errorBody()!!.string(), DoctorNotificationResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}