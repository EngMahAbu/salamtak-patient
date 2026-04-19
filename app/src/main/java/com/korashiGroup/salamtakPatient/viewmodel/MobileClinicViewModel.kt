package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.mobileClinic.MobileClinicResponse
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobileClinicViewModel @Inject constructor(val repository: RegisterRepositry) : ViewModel() {

    val mobileResponse = MutableLiveData<Resource<MobileClinicResponse>>()

    val fullName = MutableLiveData<String>()
    val fullNameError = MutableLiveData<String?>()

    val mobileNumber = MutableLiveData<String>()
    val mobileNumberError = MutableLiveData<String?>()

    val city = MutableLiveData<Int?>()
    val cityError = MutableLiveData<String?>()

    val area = MutableLiveData<Int?>()
    val areaError = MutableLiveData<String?>()

    val streetName = MutableLiveData<String>()
    val streetNameError = MutableLiveData<String?>()

    val building = MutableLiveData<String>()
    val buildingError = MutableLiveData<String?>()

    val floor = MutableLiveData<String>()
    val floorError = MutableLiveData<String?>()

    val apartment = MutableLiveData<String>()
    val apartmentError = MutableLiveData<String?>()

    val serviceLevel = MutableLiveData<Int?>()
    val serviceLevelError = MutableLiveData<String?>()

    val gender = MutableLiveData<Int>()
    val genderError = MutableLiveData<String?>()

    val subSpeciality = MutableLiveData<Int?>()
    val subSpecialityError = MutableLiveData<String?>()

    val speciality = MutableLiveData<Int?>()
    val specialityError = MutableLiveData<String?>()
    val symptoms = MutableLiveData<String>()
    val symptomsError = MutableLiveData<String?>()

    val specialRequirements = MutableLiveData<String>()
    val specialRequirementsError = MutableLiveData<String?>()

    val event = MutableLiveData<Events>()

    val message = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()




    fun validateForm(): Boolean {

        var isValid = true

        val nameParts = fullName.value?.trim()?.split("\\s+".toRegex()) ?: emptyList()

        if (nameParts.size < 3) {
            fullNameError.value = "Please enter your first, middle and last name"
            isValid = false
        } else {
            fullNameError.value = null
        }

        val mobileRegex = Regex("^01[0-9]{9}$")

        if (mobileNumber.value.isNullOrEmpty()) {
            mobileNumberError.value = "Please enter your mobile Number"
            isValid = false
        } else if (!mobileRegex.matches(mobileNumber.value!!)) {
            mobileNumberError.value = "Enter valid Egyptian phone number that include 11 numbers"
            isValid = false
        } else {
            mobileNumberError.value = null
        }

        if (city.value?.toInt().toString().isNullOrEmpty()){
            cityError.value = "Please choose your city"
            isValid = false
        }else{
            cityError.value = null
        }

        if (area.value?.toInt().toString().isNullOrEmpty()){
            areaError.value = "Please choose your area"
            isValid = false
        }else{
            areaError.value = null
        }

        if (streetName.value.isNullOrEmpty()){
            streetNameError.value = "Please enter street name"
            isValid = false
        }else{
            streetNameError.value = null
        }

        if (building.value.isNullOrEmpty()){
            buildingError.value = "Please enter your building number"
            isValid = false
        }else{
            buildingError.value = null
        }

        if (floor.value.isNullOrEmpty()){
            floorError.value = "Please enter your floor number"
            isValid = false
        }else{
            floorError.value = null
        }

        if (apartment.value.isNullOrEmpty()){
            apartmentError.value = "Please enter your apartment number"
            isValid = false
        }else{
            apartmentError.value = null
        }

        if (speciality.value?.toInt().toString().isNullOrEmpty()){
            specialityError.value = "Please choose your speciality"
            isValid = false
        }else{
            specialityError.value = null
        }

        if (subSpeciality.value?.toInt().toString().isNullOrEmpty()){
            subSpecialityError.value = "Please choose your sub speciality"
            isValid = false
        }else{
            subSpecialityError.value = null
        }

        if (serviceLevel.value?.toInt().toString().isNullOrEmpty()){
            serviceLevelError.value = "Please choose your sub service level"
            isValid = false
        }else{
            subSpecialityError.value = null
        }

        if (symptoms.value.isNullOrEmpty()){
            symptomsError.value = "Please enter your symptoms"
            isValid = false
        }else{
            symptomsError.value = null
        }

        if (specialRequirements.value.isNullOrEmpty()){
            specialRequirementsError.value = "Please enter your requirements"
            isValid = false
        }else{
            specialRequirementsError.value = null
        }
        return isValid
    }

    fun sendBtn(){
        if(!validateForm())
            return

        viewModelScope.launch(Dispatchers.IO) {

            loading.postValue(true)
            try {
                val response = repository.sendMobileClinic(
                    fullName.value.toString(),
                    mobileNumber.value.toString(),
                    speciality.value?:0,
                    building.value.toString(),
                    floor.value.toString(),
                    apartment.value.toString(),
                    streetName.value.toString(),
                    city.value?:0,
                    area.value?:0,
                    subSpeciality.value?:0,
                    serviceLevel.value?:0,
                    specialRequirements.value.toString(),
                    gender.value?:0,
                    symptoms.value.toString()

                )


                val body = response.body()

                if (body != null && body.success == true) {
                    mobileResponse.postValue(Resource.sucess(body))
                    event.postValue(Events.NavigateToMobileClinicConfirmation)
                } else if (body != null && body.success == false) {

                    mobileResponse.postValue(Resource.Error(body.message ?: "حدث خطأ أثناء الإرسال"))
                } else {

                    mobileResponse.postValue(Resource.Error("لم يتم استقبال بيانات من السيرفر"))
                }

            } catch (e: Exception) {
                val message = when (e) {
                    is java.net.UnknownHostException -> "مفيش إنترنت"
                    is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال"
                    is retrofit2.HttpException -> "مشكلة في السيرفر"
                    is javax.net.ssl.SSLHandshakeException -> "مشكلة في الاتصال الآمن"
                    else -> e.localizedMessage ?: "خطأ غير معروف"
                }
                mobileResponse.postValue(Resource.Error(message))
            }finally {
                    loading.postValue(false)
            }
        }
    }

    }

