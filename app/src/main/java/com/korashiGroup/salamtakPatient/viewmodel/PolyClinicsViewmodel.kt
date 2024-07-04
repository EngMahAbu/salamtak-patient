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
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.AnswersDoctorResponse
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.QuestionsResponse
import com.korashiGroup.salamtakPatient.model.checktoken.CheckTokenResponse
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
    val questionsResponse=MutableLiveData<Resource<QuestionsResponse>>()
    val checkTokenResponse=MutableLiveData<Resource<CheckTokenResponse>>()
    val doctorAnswerResponse=MutableLiveData<Resource<AnswersDoctorResponse>>()


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

    fun getAllQandAByDoctorId(isQuetionType:Boolean)=viewModelScope.launch(Dispatchers.IO+handler) {
        questionsResponse.postValue(Resource.Loading())
      val response=  if (isQuetionType==true){
          repositry.getAllQandAByDoctorId()
        }else{
          repositry.getAllQandA()
        }
        questionsResponse.postValue(response?.let { handleGetAllQandAByDoctorId(it) })
    }

    private fun handleGetAllQandAByDoctorId(response: Response<QuestionsResponse>): Resource<QuestionsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<QuestionsResponse>(response.errorBody()!!.string(), QuestionsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getAnswersByDoctorId(doctorID :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorAnswerResponse.postValue(Resource.Loading())
        val response= repositry.getAnswersByDoctorId(doctorID)
        doctorAnswerResponse.postValue(response?.let { handleGetAnswersByDoctorId(it) })
    }

    private fun handleGetAnswersByDoctorId(response: Response<AnswersDoctorResponse>): Resource<AnswersDoctorResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<AnswersDoctorResponse>(response.errorBody()!!.string(), AnswersDoctorResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun checkUser()=viewModelScope.launch(Dispatchers.IO+handler) {
        checkTokenResponse.postValue(Resource.Loading())
        val response=repositry.checkUser()
        checkTokenResponse.postValue(response?.let { handleCheckUser(it) })
    }

    private fun handleCheckUser(response: Response<CheckTokenResponse>): Resource<CheckTokenResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<CheckTokenResponse>(response.errorBody()!!.string(), CheckTokenResponse::class.java)
        return error.let { Resource.Error("") }
    }



    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}