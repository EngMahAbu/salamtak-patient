package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.korashiGroup.salamtakPatient.utils.Events

class MonitoringDeviceViewModel: ViewModel() {

    val fullName = MutableLiveData<String>()
    val fullNameError = MutableLiveData<String?>()

    val mobileNumber = MutableLiveData<String>()
    val mobileNumberError = MutableLiveData<String?>()

    val city = MutableLiveData<String>()
    val cityError = MutableLiveData<String?>()

    val area = MutableLiveData<String>()
    val areaError = MutableLiveData<String?>()

    val streetName = MutableLiveData<String>()
    val streetNameError = MutableLiveData<String?>()

    val building = MutableLiveData<String>()
    val buildingError = MutableLiveData<String?>()

    val floor = MutableLiveData<String>()
    val floorError = MutableLiveData<String?>()

    val apartment = MutableLiveData<String>()
    val apartmentError = MutableLiveData<String?>()

    val serviceLevel = MutableLiveData<String>()
    val serviceLevelError = MutableLiveData<String?>()

    val gender = MutableLiveData<String>()
    val genderError = MutableLiveData<String?>()

    val fromDate = MutableLiveData<String>()
    val fromDateError = MutableLiveData<String?>()

    val toDate = MutableLiveData<String>()
    val toDateError = MutableLiveData<String?>()

    val toTime = MutableLiveData<String>()
    val toTimeError = MutableLiveData<String?>()

    val fromTime = MutableLiveData<String>()
    val fromTimeError = MutableLiveData<String?>()

    val nursingServiceDays = MutableLiveData<String>()
    val nursingServiceDaysError = MutableLiveData<String?>()

    val specialRequirements = MutableLiveData<String>()
    val specialRequirementsError = MutableLiveData<String?>()

    val event = MutableLiveData<Events>()
    val dateRegex = Regex("""^(0[1-9]|1[0-2])/(0[1-9]|[12]\d|3[01])/\d{4}$""")

    val timeRegex = Regex("""^([01]\d|2[0-3]):([0-5]\d)$""")


    fun validateForm(): Boolean {

        var isValid = true

        val nameParts = fullName.value?.trim()?.split("\\s+".toRegex()) ?: emptyList()

        if (nameParts.size < 3) {
            fullNameError.value = "Please enter your first, middle and last name"
            isValid = false
        } else {
            fullNameError.value = null
        }

        if (mobileNumber.value.isNullOrEmpty()){
            mobileNumberError.value = "Please enter your mobile Number"
            isValid = false
        }else{
            mobileNumberError.value = null
        }

        if (city.value.isNullOrEmpty()){
            cityError.value = "Please choose your city"
            isValid = false
        }else{
            cityError.value = null
        }

        if (area.value.isNullOrEmpty()){
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


        if (fromDate.value.isNullOrEmpty()) {
            fromDateError.value = "Please enter your from date"
            isValid = false
        } else if (!dateRegex.matches(fromDate.value!!)) {
            fromDateError.value = "Invalid date format. Use mm/dd/yyyy"
            isValid = false
        } else {
            fromDateError.value = null
        }

        if (toDate.value.isNullOrEmpty()) {
            toDateError.value = "Please enter your to date"
            isValid = false
        } else if (!dateRegex.matches(fromDate.value!!)) {
            toDateError.value = "Invalid date format. Use mm/dd/yyyy"
            isValid = false
        } else {
            toDateError.value = null
        }

        if (fromTime.value.isNullOrEmpty()) {
            fromTimeError.value = "Please enter your from date"
            isValid = false
        } else if (!timeRegex.matches(fromDate.value!!)) {
            fromTimeError.value = "Invalid time format. Use HH/mm"
            isValid = false
        } else {
            fromTimeError.value = null
        }

        if (toTime.value.isNullOrEmpty()) {
            toTimeError.value = "Please enter your to time"
            isValid = false
        } else if (!timeRegex.matches(fromDate.value!!)) {
            toTimeError.value = "Invalid time format. Use HH/mm"
            isValid = false
        } else {
            toTimeError.value = null
        }

        if (nursingServiceDays.value.isNullOrEmpty()){
            nursingServiceDaysError.value = "Please enter your Number of Days"
            isValid = false
        }else{
            nursingServiceDaysError.value = null
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
        if(!validateForm()){
            return
        }
        event.postValue(Events.NavigateToMonitoringDeviceRequest)

    }
}