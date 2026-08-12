package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.nursingServices.nursingConfirm.NursingConfirmResponse
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NursingServiceConfirmViewModel@Inject constructor(val repository: RegisterRepositry): ViewModel() {

    val nursingConfirmResponse = MutableLiveData <Resource<NursingConfirmResponse>>()

    val event = MutableLiveData<Events>()

    val message = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()
    fun pay(){

        Log.e("PAY_DEBUG", "PAY STARTED")
        viewModelScope.launch(Dispatchers.IO) {

            loading.postValue(true)

            try {
                val response = repository.payNursingServiceConfirmation()
                Log.e("PAY_DEBUG", "API RETURNED")
                val body = response.body()
                Log.e("PAY_DEBUG", "BODY = $body")

                if (body != null && body.success == true) {

                    nursingConfirmResponse.postValue(Resource.sucess(body))

                    event.postValue(Events.NavigateToMobileClinic)
                } else if (body != null && body.success == false) {

                    nursingConfirmResponse.postValue(Resource.Error(body.message ?: "حدث خطأ أثناء الإرسال"))
                } else {

                    nursingConfirmResponse.postValue(Resource.Error("No Data"))
                }
                Log.d("API_RESPONSE", body.toString())

            }
            catch (e: Exception) {
                val message = when (e) {
                    is java.net.UnknownHostException -> "مفيش إنترنت"
                    is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال"
                    is retrofit2.HttpException -> "مشكلة في السيرفر"
                    is javax.net.ssl.SSLHandshakeException -> "مشكلة في الاتصال الآمن"
                    else -> e.localizedMessage ?: "خطأ غير معروف"
                }
                nursingConfirmResponse.postValue(Resource.Error(message))
            }finally {
                loading.postValue(false)
            }

        }

    }

}