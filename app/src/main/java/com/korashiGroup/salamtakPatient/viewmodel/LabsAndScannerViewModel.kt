package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.labsandscanner.AnalysisResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.CalenderResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabAppointementResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabReservationResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabsBranchesResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LabsAndScannerViewModel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val analysisResponse=MutableLiveData<Resource<AnalysisResponse>>()
    val labsResponse=MutableLiveData<Resource<LabsResponse>>()
    val labsBranchesResponse=MutableLiveData<Resource<LabsBranchesResponse>>()
    val createAnalysisLbsBranchesResponse=MutableLiveData<Resource<LabReservationResponse>>()
    val calenderResponse=MutableLiveData<Resource<CalenderResponse>>()
    val lapAppointementResponse=MutableLiveData<Resource<LabAppointementResponse>>()


    fun getServicesByType(typeId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        analysisResponse.postValue(Resource.Loading())
        val response=repositry.getServicesByType(typeId)
        analysisResponse.postValue(response?.let { handlegetServicesByType(it) })
    }

    private fun handlegetServicesByType(response: Response<AnalysisResponse>): Resource<AnalysisResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<AnalysisResponse>(response.errorBody()!!.string(), AnalysisResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getLabsByTypeId(typeId :Int,labName :String?=null)=viewModelScope.launch(Dispatchers.IO+handler) {
        labsResponse.postValue(Resource.Loading())
        val response=repositry.getLabsByTypeId(typeId,labName)
        labsResponse.postValue(response?.let { handlegetLabsByTypeId(it) })
    }

    private fun handlegetLabsByTypeId(response: Response<LabsResponse>): Resource<LabsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<LabsResponse>(response.errorBody()!!.string(), LabsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getLabAvailabilityByLabId(labId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        calenderResponse.postValue(Resource.Loading())
        val response=repositry.getLabAvailabilityByLabId(labId)
        calenderResponse.postValue(response?.let { handlegetLabAvailabilityByLabId(it) })
    }

    private fun handlegetLabAvailabilityByLabId(response: Response<CalenderResponse>): Resource<CalenderResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<CalenderResponse>(response.errorBody()!!.string(), CalenderResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getLabsByFilters(Request: HashMap<String, String>,labId:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        labsBranchesResponse.postValue(Resource.Loading())
        val response=if (labId==0){
            repositry.getLabsByFilters(Request)
        }else{
            repositry.getLabBranches(Request)
        }
        labsBranchesResponse.postValue(response?.let { handlegetLabsByFilters(it) })
    }

    private fun handlegetLabsByFilters(response: Response<LabsBranchesResponse>): Resource<LabsBranchesResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<LabsBranchesResponse>(response.errorBody()!!.string(), LabsBranchesResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun createLaboratoryReservation(Request: HashMap<String, String>)=viewModelScope.launch(Dispatchers.IO+handler) {
        createAnalysisLbsBranchesResponse.postValue(Resource.Loading())
        val response=repositry.createLaboratoryReservation(Request)
        createAnalysisLbsBranchesResponse.postValue(response?.let { handlecreateLaboratoryReservation(it) })
    }

    private fun handlecreateLaboratoryReservation(response: Response<LabReservationResponse>): Resource<LabReservationResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<LabReservationResponse>(response.errorBody()!!.string(), LabReservationResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientAppointments()=viewModelScope.launch(Dispatchers.IO+handler) {
        lapAppointementResponse.postValue(Resource.Loading())
        val response=repositry.getPatientAppointments()
        lapAppointementResponse.postValue(response?.let { handleGetPatientAppointments(it) })
    }

    private fun handleGetPatientAppointments(response: Response<LabAppointementResponse>): Resource<LabAppointementResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<LabAppointementResponse>(response.errorBody()!!.string(), LabAppointementResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}