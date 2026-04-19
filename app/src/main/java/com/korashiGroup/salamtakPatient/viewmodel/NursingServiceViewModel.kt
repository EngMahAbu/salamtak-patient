package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.nursingServices.NursingResponse
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NursingServiceViewModel@Inject constructor(val repository: RegisterRepositry) : ViewModel() {

     val nursingResponse = MutableLiveData <Resource<NursingResponse>>()
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

    val numOfDays = MutableLiveData<String>()
    val numOfDaysError = MutableLiveData<String?>()


    val serviceLevel = MutableLiveData<Int?>()
    val serviceLevelError = MutableLiveData<String?>()

    val gender = MutableLiveData<Int>()
    val genderError = MutableLiveData<String?>()

    val fromDate = MutableLiveData<String>()
    val fromDateError = MutableLiveData<String?>()

    val toDate = MutableLiveData<String>()
    val toDateError = MutableLiveData<String?>()

    val fromTime = MutableLiveData<String>()
    val fromTimeError = MutableLiveData<String?>()

    val toTime = MutableLiveData<String>()
    val toTimeError = MutableLiveData<String?>()


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

        if (city.value == null || city.value == 0) {
            cityError.value = "Please choose your city"
            isValid = false
        } else {
            cityError.value = null
        }

        if (area.value == null || area.value == 0) {
            areaError.value = "Please choose your area"
            isValid = false
        } else {
            areaError.value = null
        }

        if (streetName.value.isNullOrEmpty()) {
            streetNameError.value = "Please enter street name"
            isValid = false
        } else {
            streetNameError.value = null
        }

        if (building.value.isNullOrEmpty()) {
            buildingError.value = "Please enter your building number"
            isValid = false
        } else {
            buildingError.value = null
        }

        if (floor.value.isNullOrEmpty()) {
            floorError.value = "Please enter your floor number"
            isValid = false
        } else {
            floorError.value = null
        }

        if (apartment.value.isNullOrEmpty()) {
            apartmentError.value = "Please enter your apartment number"
            isValid = false
        } else {
            apartmentError.value = null
        }


        if (serviceLevel.value == null || serviceLevel.value == 0) {
            serviceLevelError.value = "Please choose your sub service level"
            isValid = false
        } else {
            serviceLevelError.value = null
        }

        if (gender.value == null || gender.value == 0) {
            genderError.value = "Please choose your gender"
            isValid = false
        } else {
            genderError.value = null
        }

        val dateRegex = Regex("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$")

        if (fromDate.value.isNullOrEmpty()) {
            fromDateError.value = "Please enter from date"
            isValid = false
        } else if (!dateRegex.matches(fromDate.value!!)) {
            fromDateError.value = "Invalid date format (MM/DD/YYYY)"
            isValid = false
        } else {
            fromDateError.value = null
        }

        if (toDate.value.isNullOrEmpty()) {
            toDateError.value = "Please enter to date"
            isValid = false
        } else if (!dateRegex.matches(toDate.value!!)) {
            toDateError.value = "Invalid date format (MM/DD/YYYY)"
            isValid = false
        } else {
            toDateError.value = null
        }

        val timeRegex = Regex("^([01][0-9]|2[0-3]):[0-5][0-9]$")

        if (fromTime.value.isNullOrEmpty()) {
            fromTimeError.value = "Please enter from time"
            isValid = false
        } else if (!timeRegex.matches(fromTime.value!!)) {
            fromTimeError.value = "Invalid time format (HH:MM)"
            isValid = false
        } else {
            fromTimeError.value = null
        }

        if (toTime.value.isNullOrEmpty()) {
            toTimeError.value = "Please enter to time"
            isValid = false
        } else if (!timeRegex.matches(toTime.value!!)) {
            toTimeError.value = "Invalid time format (HH:MM)"
            isValid = false
        } else {
            toTimeError.value = null
        }


        if (numOfDays.value.isNullOrEmpty()) {
            numOfDaysError.value = "Please enter number of days"
            isValid = false
        } else {
            numOfDaysError.value = null
        }
        return isValid
    }

    fun sendNursingService(){

        if (!validateForm()){

                        Log.e("VALIDATION_DEBUG", """
            fullName = ${fullName.value}
            mobile = ${mobileNumber.value}
            city = ${city.value}
            area = ${area.value}
            street = ${streetName.value}
            building = ${building.value}
            floor = ${floor.value}
            apartment = ${apartment.value}
            serviceLevel = ${serviceLevel.value}
            gender = ${gender.value}
            fromDate = ${fromDate.value}
            toDate = ${toDate.value}
            fromTime = ${fromTime.value}
            toTime = ${toTime.value}
            numOfDays = ${numOfDays.value}
            """.trimIndent())

            Log.e("VALIDATION", "FAILED")
            message.postValue("Validation Failed ❌")
            return
        }

//        Log.e("VALIDATION", "PASSED ✅")

        viewModelScope.launch(Dispatchers.IO) {

            loading.postValue(true)

            try {
                  val response = repository.sendNursingService(
                    fullName.value.toString(),
                    mobileNumber.value.toString(),
                    building.value.toString(),
                    streetName.value.toString(),
                    floor.value.toString(),
                    apartment.value.toString(),
                     city.value?:0,
                    area.value?:0,
                    gender.value?:0,
                    fromDate.value.toString(),
                    toDate.value.toString(),
                     fromTime.value.toString(),
                     toTime.value.toString(),
                      serviceLevel.value?:0,
                      numOfDays.value.toString()
                  )
                val body = response.body()

                if (body != null && body.success == true) {
                    nursingResponse.postValue(Resource.sucess(body))
                    event.postValue(Events.NavigateToMobileClinicConfirmation)
                } else if (body != null && body.success == false) {

                    nursingResponse.postValue(Resource.Error(body.message ?: "حدث خطأ أثناء الإرسال"))
                } else {

                    nursingResponse.postValue(Resource.Error("لم يتم استقبال بيانات من السيرفر"))
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
                nursingResponse.postValue(Resource.Error(message))
            }finally {
                loading.postValue(false)
            }

        }

    }













}