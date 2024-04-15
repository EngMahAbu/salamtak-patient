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
import com.korashiGroup.salamtakPatient.model.docotorsearch.NewDoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCardResponse
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompanyResponse
import com.korashiGroup.salamtakPatient.model.insurance.SendInsuranceCardResponce
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
class DialogBottomSheetViewmodel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){

    var doctorSearchPage=1
    private var doctorSearchItemResponse:DoctorsearchItemResponse?=null
    var healthEntityPage=1
    var healthEntityResponse:GetHealthEntityResponse?=null
    val nationalityResponse=MutableLiveData<Resource<GetCountry>>()
    val specialistResponse=MutableLiveData<Resource<GetSpecialistResponse>>()
    val subSpecialistResponse=MutableLiveData<Resource<GetSubSpecilistResponse>>()
    val seniorityLevelResponse=MutableLiveData<Resource<GetSeniorityLevelResponse>>()
    val allCitiesResponse=MutableLiveData<Resource<GetAllCitiesResponse>>()
    val allAreasByCityIdResponse=MutableLiveData<Resource<GetAreasByCityIdResponse>>()
    val docorsResponse=MutableLiveData<Resource<DoctorsearchItemResponse>>()
    val medicalExamination=MutableLiveData<Resource<MedicalExaminationResponse>>()
    val bookingResponse=MutableLiveData<Resource<BookingResponse>>()
    val patientAppointmentResponse=MutableLiveData<Resource<CreatepatientAppointementsResponse>>()
    val doctorClinkResponse=MutableLiveData<Resource<GetDoctorClinksResponse>>()
    val patientsAppointmentesResponse=MutableLiveData<Resource<GetPatientAppointmentesResponse>>()
    val cancelPatientResponse=MutableLiveData<Resource<CancelPatientAppointmentResponse>>()
    val patientProfileResponse=MutableLiveData<Resource<PatientProfileResponse>>()
    val patientLocationResponse=MutableLiveData<Resource<PatientLocationResponse>>()
    val healthyResponse=MutableLiveData<Resource<GetHealthEntityResponse>>()
    val popularResponse=MutableLiveData<Resource<List<PopularDoctorsResponseItem>>>()
    val healthTopicsResponse=MutableLiveData<Resource<GetDoctorHealthTopicsResponse>>()
    val doctorSpotLightResponse=MutableLiveData<Resource<GetDoctorSpotLightResponse>>()
    val updatePatientProfileResponse=MutableLiveData<Resource<UpdateProfilePatientResponse>>()
    val patientMedicalInfoResponse=MutableLiveData<Resource<PatientMedicalInfoResponse>>()
    val bloodTypesResponse=MutableLiveData<Resource<BloodTypeResponse>>()
    val medicineAllergyResponse=MutableLiveData<Resource<MedicineAllergyResponse>>()
    val foodAllergyResponse=MutableLiveData<Resource<FoodAllergyResponse>>()
    val patientInfoResponse=MutableLiveData<Resource<GetPatientInfoResponse>>()
    val patientMedicalInfo=MutableLiveData<Resource<PatientMedicalInfo>>()
    val InsuranceCompany=MutableLiveData<Resource<InsuranceCompanyResponse>>()
    val gatesResponse=MutableLiveData<Resource<SalamtakGateResponse>>()
    val blogsResponse=MutableLiveData<Resource<BlogsResponse>>()
    val questionAndAnswerResponse=MutableLiveData<Resource<QuestionAndAnswerResponse>>()
    val insuranceResponse=MutableLiveData<Resource<SendInsuranceCardResponce>>()
    val insurancecardResponse=MutableLiveData<Resource<InsuranceCardResponse>>()




    fun getSpecialist()=viewModelScope.launch(Dispatchers.IO+handler) {
        specialistResponse.postValue(Resource.Loading())
        val response=repositry.getSpecialist()
        specialistResponse.postValue(response?.let { handleGetSpecialist(it) })
    }

    private fun handleGetSpecialist(response: Response<GetSpecialistResponse>): Resource<GetSpecialistResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())    }

    fun getCountries()=viewModelScope.launch(Dispatchers.IO+handler) {
        nationalityResponse.postValue(Resource.Loading())
        val response=repositry.getCountries()
        nationalityResponse.postValue(response?.let { handleGetCountries(it) })
    }

    private fun handleGetCountries(response: Response<GetCountry>): Resource<GetCountry>? {
        if (response.isSuccessful){
            response.body()?.let {
               return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getSubSpecialist(specialListId:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        subSpecialistResponse.postValue(Resource.Loading())
        val response=repositry.getSubSpecialist(specialListId)
        subSpecialistResponse.postValue(response?.let { handelGetSUBSpecialist(it) })
    }

    private fun handelGetSUBSpecialist(response: Response<GetSubSpecilistResponse>): Resource<GetSubSpecilistResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getBloodTypes()=viewModelScope.launch(Dispatchers.IO+handler) {
        bloodTypesResponse.postValue(Resource.Loading())
        val response=repositry.getBloodTypes()
        bloodTypesResponse.postValue(response?.let { handelgetBloodTypes(it) })
    }

    private fun handelgetBloodTypes(response: Response<BloodTypeResponse>): Resource<BloodTypeResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<BloodTypeResponse>(response.errorBody()!!.string(), BloodTypeResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getMedicineAllergy()=viewModelScope.launch(Dispatchers.IO+handler) {
        medicineAllergyResponse.postValue(Resource.Loading())
        val response=repositry.getMedicineAllergy()
        medicineAllergyResponse.postValue(response?.let { handelgetMedicineAllergys(it) })
    }
    private fun handelgetMedicineAllergys(response: Response<MedicineAllergyResponse>): Resource<MedicineAllergyResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<MedicineAllergyResponse>(response.errorBody()!!.string(), MedicineAllergyResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getFoodAllergy()=viewModelScope.launch(Dispatchers.IO+handler) {
        foodAllergyResponse.postValue(Resource.Loading())
        val response=repositry.getFoodAllergy()
        foodAllergyResponse.postValue(response?.let { handelGetFoodAllergy(it) })
    }
    private fun handelGetFoodAllergy(response: Response<FoodAllergyResponse>): Resource<FoodAllergyResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<FoodAllergyResponse>(response.errorBody()!!.string(), FoodAllergyResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getSeniorityLevel()=viewModelScope.launch(Dispatchers.IO+handler) {
        seniorityLevelResponse.postValue(Resource.Loading())
        val response=repositry.getSeniorityLevel()
        seniorityLevelResponse.postValue(response?.let { handelGetseniorityLevel(it) })
    }

    private fun handelGetseniorityLevel(response: Response<GetSeniorityLevelResponse>): Resource<GetSeniorityLevelResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())

    }

    fun getAllCities()=viewModelScope.launch(Dispatchers.IO+handler) {
        allCitiesResponse.postValue(Resource.Loading())
        val response=repositry.getAllCities()
        allCitiesResponse.postValue(response?.let { handelGetAllCities(it) })
    }

    private fun handelGetAllCities(response: Response<GetAllCitiesResponse>): Resource<GetAllCitiesResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())    }

    fun getAreasByCityId(cityId:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        allAreasByCityIdResponse.postValue(Resource.Loading())
        val response=repositry.getAreasByCityId(cityId)
        allAreasByCityIdResponse.postValue(response?.let { handelgetAreasByCityId(it) })
    }

    private fun handelgetAreasByCityId(response: Response<GetAreasByCityIdResponse>): Resource<GetAreasByCityIdResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }

     fun searchDoctors(doctorSearchRequest: DoctorSearchRequest, new:String )=viewModelScope.launch(Dispatchers.IO+handler) {
       if (new=="new"){
           val newDoctorSearchRequest=NewDoctorSearchRequest(doctorSearchRequest.cityId,doctorSearchRequest.areaId,doctorSearchRequest.maxResultCount,
               doctorSearchRequest.medicalExaminationTypeId,doctorSearchRequest.skipCount,doctorSearchRequest.specialistId)
           docorsResponse.postValue(Resource.Loading())
           val response=repositry.doctorSearch3(newDoctorSearchRequest)
           docorsResponse.postValue(response?.let { handlingSearchDoctors(it) })
       }else{
           docorsResponse.postValue(Resource.Loading())
           val response=repositry.searchDoctors(doctorSearchRequest)
           docorsResponse.postValue(response?.let { handlingSearchDoctors(it) })
       }
    }

    private fun handlingSearchDoctors(response: Response<DoctorsearchItemResponse>): Resource<DoctorsearchItemResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                doctorSearchPage+=10
                if (doctorSearchItemResponse==null){
                    doctorSearchItemResponse=it
                }else {
                    val oldItems = doctorSearchItemResponse?.data?.items
                    val newItems = it.data?.items
                    if (newItems != null) {
                        oldItems?.addAll(newItems)
                    }
                }
                return Resource.sucess(it)
            }
        }
   //    val error = Gson().fromJson<DoctorsearchItemResponse>(response.errorBody()!!.string(), DoctorsearchItemResponse::class.java)
        return Resource.Error(response.message())

    }

    fun getMedicalExaminationType()=viewModelScope.launch(Dispatchers.IO+handler) {
        medicalExamination.postValue(Resource.Loading())
        val response=repositry.getMedicalExamination()
        medicalExamination.postValue(response?.let { handleGetMedicalExamination(it) })
    }

    private fun handleGetMedicalExamination(response: Response<MedicalExaminationResponse>): Resource<MedicalExaminationResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getClinicSchedualByClinicDayId(ClinicId: Int,DayId:Int,
                                       MedicalExaminationTypeId:Int,
                                       BookDate:String)=viewModelScope.launch(Dispatchers.IO+handler) {
        bookingResponse.postValue(Resource.Loading())
        val response=repositry.getClinicSchedualByClinicDayId(ClinicId,DayId,MedicalExaminationTypeId,BookDate)
        bookingResponse.postValue(response?.let { handleGetClinicSchedualByClinicDayId(it) })
    }

    private fun handleGetClinicSchedualByClinicDayId(response: Response<BookingResponse>): Resource<BookingResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun createPatientAppointment(appointmentRequest: PatientAppointmentRequest)=viewModelScope.launch(Dispatchers.IO+handler) {
        patientAppointmentResponse.postValue(Resource.Loading())
        val response=repositry.createPatientAppointment(appointmentRequest)
        patientAppointmentResponse.postValue(response?.let { handlecreatePatientAppointment(it) })
    }

    private fun handlecreatePatientAppointment(response: Response<CreatepatientAppointementsResponse>): Resource<CreatepatientAppointementsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<CreatepatientAppointementsResponse>(response.errorBody()!!.string(), CreatepatientAppointementsResponse::class.java)
        return Resource.Error(error.message)
    }

    fun getDoctorProfileByDoctorId(DoctorId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorClinkResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorProfileByDoctorId(DoctorId)
        doctorClinkResponse.postValue(response?.let { handleGetDoctorProfileByDoctorId(it) })
    }

    private fun handleGetDoctorProfileByDoctorId(response: Response<GetDoctorClinksResponse>): Resource<GetDoctorClinksResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<GetDoctorClinksResponse>(response.errorBody()!!.string(), GetDoctorClinksResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientAppointmentes(medicalExaminationTypeId :Int?)=viewModelScope.launch(Dispatchers.IO+handler) {
        patientsAppointmentesResponse.postValue(Resource.Loading())
        val response=repositry.getPatientAppointmentes(medicalExaminationTypeId)
        patientsAppointmentesResponse.postValue(response?.let { handleGetPatientAppointmentes(it) })
    }

    private fun handleGetPatientAppointmentes(response: Response<GetPatientAppointmentesResponse>): Resource<GetPatientAppointmentesResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<GetPatientAppointmentesResponse>(response.errorBody()!!.string(), GetPatientAppointmentesResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun cancelPatientAppointment(AppointmentId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        cancelPatientResponse.postValue(Resource.Loading())
        val response=repositry.cancelPatientAppointment(AppointmentId)
        cancelPatientResponse.postValue(response?.let { handleCancelPatientAppointment(it) })
    }

    private fun handleCancelPatientAppointment(response: Response<CancelPatientAppointmentResponse>): Resource<CancelPatientAppointmentResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }

        val error = Gson().fromJson<CancelPatientAppointmentResponse>(response.errorBody()!!.string(), CancelPatientAppointmentResponse::class.java)
        return error.message?.let { Resource.Error(it) }

    }
    fun createPatientProfile(partmap: MultipartBody)= viewModelScope.launch(Dispatchers.IO+handler) {
        patientProfileResponse.postValue(Resource.Loading())
        val response = repositry.createPatientProfile(partmap)

        patientProfileResponse.postValue(response?.let { handlePatientProfile(it) })
    }

    private fun handlePatientProfile(response: Response<PatientProfileResponse>): Resource<PatientProfileResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }

        val error = Gson().fromJson<PatientProfileResponse>(response.errorBody()!!.string(), PatientProfileResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun updatePatientProfile(partmap: MultipartBody)= viewModelScope.launch(Dispatchers.IO+handler) {
        updatePatientProfileResponse.postValue(Resource.Loading())
        val response = repositry.updatePatientProfile(partmap)

        updatePatientProfileResponse.postValue(response?.let { handleupdatePatientProfile(it) })
    }

    private fun handleupdatePatientProfile(response: Response<UpdateProfilePatientResponse>): Resource<UpdateProfilePatientResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<UpdateProfilePatientResponse>(response.errorBody()!!.string(), UpdateProfilePatientResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun sendPatientLocation(locationRequest: LocationRequest)= viewModelScope.launch(Dispatchers.IO+handler) {
        patientLocationResponse.postValue(Resource.Loading())
        val response = repositry.sendPatientLocation(locationRequest)
        patientLocationResponse.postValue(response?.let { handlePatientLocation(it) })
    }

    private fun handlePatientLocation(response: Response<PatientLocationResponse>): Resource<PatientLocationResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PatientLocationResponse>(response.errorBody()!!.string(), PatientLocationResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

     fun getHealthEntityPagedList(CityId :Int,AreaId :Int,HealthEntityTypeId :Int,
                                         MaxResultCount :Int,healthEntityPage:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        healthyResponse.postValue(Resource.Loading())
        val response = repositry.getHealthEntityPagedList(CityId,AreaId,HealthEntityTypeId,MaxResultCount,healthEntityPage)

        healthyResponse.postValue(response?.let { handleGetHealthEntityPagedList(it) })
    }

    private fun handleGetHealthEntityPagedList(response: Response<GetHealthEntityResponse>): Resource<GetHealthEntityResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                healthEntityPage+=10
                if (healthEntityResponse==null){
                    healthEntityResponse=it
                }else{
                    val oldItems=healthEntityResponse?.data?.items
                    val newItems=it.data?.items
                    if (newItems != null) {
                        oldItems?.addAll(newItems)
                    }

                }

                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<GetHealthEntityResponse>(response.errorBody()!!.string(), GetHealthEntityResponse::class.java)
        return error?.message?.let { Resource.Error(it)}
    }

    fun getPopularDoctors()=viewModelScope.launch(Dispatchers.IO+handler) {
        popularResponse.postValue(Resource.Loading())
        val response=repositry.getPopularDoctors()
        popularResponse.postValue(response?.let { handlegetPopularDoctors(it) })
    }

    private fun handlegetPopularDoctors(response: Response<List<PopularDoctorsResponseItem>>): Resource<List<PopularDoctorsResponseItem>>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getDoctorHealthTopics()=viewModelScope.launch(Dispatchers.IO+handler) {
        healthTopicsResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorHealthTopics()
        healthTopicsResponse.postValue(response?.let { handlegetDoctorHealthTopics(it) })
    }

    private fun handlegetDoctorHealthTopics(response: Response<GetDoctorHealthTopicsResponse>): Resource<GetDoctorHealthTopicsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getDoctorSpotLight()=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorSpotLightResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorSpotLight()
        doctorSpotLightResponse.postValue(response?.let { handleGetDoctorSpotLight(it) })
    }

    private fun handleGetDoctorSpotLight(response: Response<GetDoctorSpotLightResponse>): Resource<GetDoctorSpotLightResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun createPatientMedicalInfo(medicalInfoRequest: HashMap<String,String>)=viewModelScope.launch(Dispatchers.IO+handler) {
        patientMedicalInfoResponse.postValue(Resource.Loading())
        val response=repositry.CreatePatientMedicallInfo(medicalInfoRequest)
        patientMedicalInfoResponse.postValue(response?.let { handlecreatePatientMedicallInfo(it) })
    }

    private fun handlecreatePatientMedicallInfo(response: Response<PatientMedicalInfoResponse>): Resource<PatientMedicalInfoResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PatientMedicalInfoResponse>(response.errorBody()!!.string(), PatientMedicalInfoResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientInfo()=viewModelScope.launch(Dispatchers.IO+handler) {
        patientInfoResponse.postValue(Resource.Loading())
        val response=repositry.getPatientInfo()
        patientInfoResponse.postValue(response?.let { handlegetPatientInfo(it) })
    }

    private fun handlegetPatientInfo(response: Response<GetPatientInfoResponse>): Resource<GetPatientInfoResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<GetPatientInfoResponse>(response.errorBody()!!.string(), GetPatientInfoResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientMedicalInfo()=viewModelScope.launch(Dispatchers.IO+handler) {
        patientMedicalInfo.postValue(Resource.Loading())
        val response=repositry.getPatientMedicalInfo()
        patientMedicalInfo.postValue(response?.let { handleGetPatientMedicalInfo(it) })
    }

    private fun handleGetPatientMedicalInfo(response: Response<PatientMedicalInfo>): Resource<PatientMedicalInfo>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<PatientMedicalInfo>(response.errorBody()!!.string(), PatientMedicalInfo::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getAllInsurance_Companies()=viewModelScope.launch(Dispatchers.IO+handler) {
        InsuranceCompany.postValue(Resource.Loading())
        val response=repositry.getAllInsurance_Companies()
        InsuranceCompany.postValue(response?.let { handleGetAllInsurance_Companies(it) })
    }

    private fun handleGetAllInsurance_Companies(response: Response<InsuranceCompanyResponse>): Resource<InsuranceCompanyResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<InsuranceCompanyResponse>(response.errorBody()!!.string(), InsuranceCompanyResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getSalamtakGate()=viewModelScope.launch(Dispatchers.IO+handler) {
        gatesResponse.postValue(Resource.Loading())
        val response=repositry.getSalamtakGate()
        gatesResponse.postValue(response?.let { handleGetgatesResponse(it) })
    }

    private fun handleGetgatesResponse(response: Response<SalamtakGateResponse>): Resource<SalamtakGateResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<SalamtakGateResponse>(response.errorBody()!!.string(), SalamtakGateResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getBlogsGate(gateId:Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        blogsResponse.postValue(Resource.Loading())
        val response=repositry.getBlogs(gateId)
        blogsResponse.postValue(response?.let { handlegetBlogsGateResponse(it) })
    }

    private fun handlegetBlogsGateResponse(response: Response<BlogsResponse>): Resource<BlogsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<BlogsResponse>(response.errorBody()!!.string(), BlogsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun createQandAForPatient( questionRequest:MultipartBody)=viewModelScope.launch(Dispatchers.IO+handler) {
        questionAndAnswerResponse.postValue(Resource.Loading())
        val response=repositry.createQandAForPatient(questionRequest)
        questionAndAnswerResponse.postValue(response?.let { handlecreateQandAForPatient(it) })
    }

    private fun handlecreateQandAForPatient(response: Response<QuestionAndAnswerResponse>): Resource<QuestionAndAnswerResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<QuestionAndAnswerResponse>(response.errorBody()!!.string(), QuestionAndAnswerResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun createPatient_Insurance( questionRequest:MultipartBody)=viewModelScope.launch(Dispatchers.IO+handler) {
        insuranceResponse.postValue(Resource.Loading())
        val response=repositry.createPatient_Insurance(questionRequest)
        insuranceResponse.postValue(response?.let { handlecreatePatient_Insurance(it) })
    }

    private fun handlecreatePatient_Insurance(response: Response<SendInsuranceCardResponce>): Resource<SendInsuranceCardResponce>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<SendInsuranceCardResponce>(response.errorBody()!!.string(), SendInsuranceCardResponce::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getPatient_Insurance( )=viewModelScope.launch(Dispatchers.IO+handler) {
        insurancecardResponse.postValue(Resource.Loading())
        val response=repositry.getPatient_Insurance()
        insurancecardResponse.postValue(response?.let { handlegetPatient_Insurance(it) })
    }

    private fun handlegetPatient_Insurance(response: Response<InsuranceCardResponse>): Resource<InsuranceCardResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<InsuranceCardResponse>(response.errorBody()!!.string(), InsuranceCardResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }




    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","osama${exception.message.toString()}")
    }
}