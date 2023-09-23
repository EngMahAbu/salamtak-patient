package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.booking.*
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorsearchItemResponse
import com.korashiGroup.salamtakPatient.model.healthy.GetHealthEntityResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.GetDoctorHealthTopicsResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.GetDoctorSpotLightResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorsResponseItem
import com.korashiGroup.salamtakPatient.model.profile.*
import com.korashiGroup.salamtakPatient.model.register.*
import com.korashiGroup.salamtakPatient.model.scheduling.CancelPatientAppointmentResponse
import com.korashiGroup.salamtakPatient.model.scheduling.GetPatientAppointmentesResponse
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.askandanswer.QuestionAndAnswerResponse
import com.korashiGroup.salamtakPatient.model.askandanswer.QuestionRequest
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCardResponse
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompanyResponse
import com.korashiGroup.salamtakPatient.model.insurance.SendInsuranceCardResponce
import com.korashiGroup.salamtakPatient.model.rating.RatingResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.BlogsResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.SalamtakGateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PatientViewmodel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val doctorRateResponse=MutableLiveData<Resource<RatingResponse>>()


    fun createDoctorRate(doctorRateRequest: HashMap<String, String> )=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorRateResponse.postValue(Resource.Loading())
        val response=repositry.createDoctorRate(doctorRateRequest)
        doctorRateResponse.postValue(response?.let { handlecreateDoctorRate(it) })
    }

    private fun handlecreateDoctorRate(response: Response<RatingResponse>): Resource<RatingResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<RatingResponse>(response.errorBody()!!.string(), RatingResponse::class.java)
        return error.Message?.let { Resource.Error(it) }
    }




    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","osama${exception.message.toString()}")
    }
}