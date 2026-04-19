package com.korashiGroup.salamtakPatient.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.medicalConsultant.MedicalConsultantResponse
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MedicalConsultantViewModel @Inject constructor(val repository: RegisterRepositry) : ViewModel() {


    val medicalResponse = MutableLiveData<Resource<MedicalConsultantResponse>>()

    val fullName = MutableLiveData<String>()
    val fullNameError = MutableLiveData<String?>()

    val mobileNumber = MutableLiveData<String>()
    val mobileNumberError = MutableLiveData<String?>()
    val speciality = MutableLiveData<Int>()
    val specialityError = MutableLiveData<String?>()
    val symptoms = MutableLiveData<String>()
    val symptomsError = MutableLiveData<String?>()
    val askQuestion = MutableLiveData<String>()
    val askQuestionError = MutableLiveData<String?>()

    val event = MutableLiveData<Events>()

    val message = MutableLiveData<String>()



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

        if (speciality.value.toInt().toString().isNullOrEmpty()) {
            specialityError.value = "Please choose your speciality"
            isValid = false
        } else {
            specialityError.value = null
        }

        if (symptoms.value.isNullOrEmpty()) {
            symptomsError.value = "Please enter your symptoms"
            isValid = false
        } else {
            symptomsError.value = null
        }

        if (askQuestion.value.isNullOrEmpty()) {
            askQuestionError.value = "Please enter your question"
            isValid = false
        } else {
            askQuestionError.value = null
        }
        return isValid
    }

    fun sendBtn() {

        if (!validateForm()) return

        viewModelScope.launch(Dispatchers.IO) {

            medicalResponse.postValue(Resource.Loading())

            try {
                val response = repository.sendMedicalConsultant(
                    fullName.value.toString(),
                    mobileNumber.value.toString(),
                    speciality.value?: 0,
                    symptoms.value.toString(),
                    askQuestion.value.toString()
                )

                val body = response.body()

                if (body != null && body.success == true) {
                    medicalResponse.postValue(Resource.sucess(body))
                    event.postValue(Events.NavigateToMobileClinic)
                } else if (body != null && body.success == false) {

                    medicalResponse.postValue(Resource.Error(body.message ?: "حدث خطأ أثناء الإرسال"))
                } else {

                    medicalResponse.postValue(Resource.Error("لم يتم استقبال بيانات من السيرفر"))
                }

            } catch (e: Exception) {
                val message = when (e) {
                    is java.net.UnknownHostException -> "مفيش إنترنت"
                    is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال"
                    is retrofit2.HttpException -> "مشكلة في السيرفر"
                    is javax.net.ssl.SSLHandshakeException -> "مشكلة في الاتصال الآمن"
                    else -> e.localizedMessage ?: "خطأ غير معروف"
                }
                medicalResponse.postValue(Resource.Error(message))
            }
        }
    }
}

