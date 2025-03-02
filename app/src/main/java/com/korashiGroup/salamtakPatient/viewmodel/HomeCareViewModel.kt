package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.homecare.GetHomeCareServicesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeCareViewModel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val homeCareServicesResponse=MutableLiveData<Resource<GetHomeCareServicesResponse>>()


    fun getHomeCareService()=viewModelScope.launch(Dispatchers.IO+handler) {
        homeCareServicesResponse.postValue(Resource.Loading())
        val response=repositry.getHomeCareService()
        homeCareServicesResponse.postValue(response?.let { handlegetHomeCareService(it) })
    }

    private fun handlegetHomeCareService(response: Response<GetHomeCareServicesResponse>): Resource<GetHomeCareServicesResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)

            }
        }
        val error = Gson().fromJson<GetHomeCareServicesResponse>(response.errorBody()!!.string(), GetHomeCareServicesResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}