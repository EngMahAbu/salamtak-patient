package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.korashiGroup.salamtakPatient.adapter.GetAllAreaAdapter
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.profile.DataBloodType
import com.korashiGroup.salamtakPatient.model.profile.DataFoodAllergy
import com.korashiGroup.salamtakPatient.model.profile.DataMedicineAllergy
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.ChoosePatient
import com.korashiGroup.salamtakPatient.model.register.DataCountry
import dagger.hilt.android.lifecycle.HiltViewModel

class SharedDataViewmodel:ViewModel() {

    val specialication=MutableLiveData<SpecialistData>()
    val subSpecialication=MutableLiveData<List<SubSpecialistData>>()
    val seniorityLevelData=MutableLiveData<SeniorityLevelData>()
    val getCity=MutableLiveData<CityData>()
    val getArea=MutableLiveData<AreaData>()
    val doctorId=MutableLiveData<Int>()
    val profileStatus=MutableLiveData<Int>()
    val birthDate=MutableLiveData<String>()
    val releaeDate=MutableLiveData<String>()
    val endDate=MutableLiveData<String>()
    val chooseGender=MutableLiveData<ChooseGender>()
    val country=MutableLiveData<DataCountry>()
    val ClinicSchedualByClinicDayId=MutableLiveData<ClinicSchedualByClinicDayId>()
    val ExaminationTypeId=MutableLiveData<ImageServices>()
    val updateProfileStatus=MutableLiveData<Boolean>()
    val bloodTypesList= MutableLiveData<DataBloodType>()
    val medicineAllergyList=MutableLiveData<List<DataMedicineAllergy>>()
    val foodAllergyList=MutableLiveData<List<DataFoodAllergy>>()
    val medicalInfoStatus=MutableLiveData<Boolean>()
    val skipLoginStatus=MutableLiveData<String>()
    val patientType=MutableLiveData<ChoosePatient>()



    fun getspecialication(specialist:SpecialistData){
        specialication.postValue(specialist)
    }

    fun getSubSpecialication(subSpecialist:List<SubSpecialistData>){
        subSpecialication.postValue(subSpecialist)
    }
    fun getMedicineAllergy(medicineAllergy:List<DataMedicineAllergy>){
        medicineAllergyList.postValue(medicineAllergy)
    }
    fun getFoodAllergy(foodAllergy:List<DataFoodAllergy>){
        foodAllergyList.postValue(foodAllergy)
    }

    fun getBloodTypes(bloodTypes:DataBloodType){
        bloodTypesList.postValue(bloodTypes)
    }

    fun getSeniorityLevelData(seniorityLevel:SeniorityLevelData){
        seniorityLevelData.postValue(seniorityLevel)
    }

    fun getCity(city:CityData){
        getCity.postValue(city)
    }
    fun getArea(area:AreaData){
        getArea.postValue(area)
    }

    fun getGender(gender: ChooseGender){
        chooseGender.postValue(gender)
    }

    fun getPatientType(type: ChoosePatient){
        patientType.postValue(type)
    }

    fun getClinicSchedualByClinicDayId(ClinicSchedual:ClinicSchedualByClinicDayId){
        ClinicSchedualByClinicDayId.postValue(ClinicSchedual)
    }

    fun getDocotorId(doctorid: Int){
        doctorId.postValue(doctorid)
    }

    fun getProfileStatus(profilestatus: Int){
        profileStatus.postValue(profilestatus)
    }
    fun getBirthDate(birthdate: String){
        this.birthDate.postValue(birthdate)
    }

    fun getReleaseDate(releaseDate: String){
        this.releaeDate.postValue(releaseDate)
    }

    fun getEdndDate(enddate: String){
        this.endDate.postValue(enddate)
    }
    fun getCountry(selectCountry: DataCountry){
        country.postValue(selectCountry)
    }
    fun getMedicalExaminationTypeId(medicalExamination: ImageServices){
        ExaminationTypeId.postValue(medicalExamination)
    }

    fun getUpdateProfileStatus(status:Boolean){
        updateProfileStatus.postValue(status)
    }

    fun getMedicalInfoStatus(status:Boolean){
        medicalInfoStatus.postValue(status)
    }

    fun getSkipLoginStatus(skip:String){
        skipLoginStatus.postValue(skip)
    }
}