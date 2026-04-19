package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.korashiGroup.salamtakPatient.fragment.labsandscanner.model.LabsInfo
import com.korashiGroup.salamtakPatient.fragment.offers.model.Offer
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.booking.clink.PolyClinicData
import com.korashiGroup.salamtakPatient.model.nursingServices.service.ServiceLevelData
import com.korashiGroup.salamtakPatient.model.patient.DataRelation
import com.korashiGroup.salamtakPatient.model.profile.DataBloodType
import com.korashiGroup.salamtakPatient.model.profile.DataFoodAllergy
import com.korashiGroup.salamtakPatient.model.profile.DataMedicineAllergy
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.ChoosePatient
import com.korashiGroup.salamtakPatient.model.register.DataCountry
import com.korashiGroup.salamtakPatient.model.register.RatingDoctors

class SharedDataViewmodel:ViewModel() {

    val specialication=MutableLiveData<SpecialistData>()
    val subSpecialication=MutableLiveData<List<SubSpecialistData>>()
    val seniorityLevelData=MutableLiveData<SeniorityLevelData>()
    val serviceLevelData=MutableLiveData<ServiceLevelData>()
    val notificationNumber = MutableLiveData<Pair<Int, Int>>()
    val getCity=MutableLiveData<CityData>()
    val relationResponse=MutableLiveData<DataRelation>()
    val clinicIdAndDoctorId=MutableLiveData<ClinicData>()
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
    val navigateStatusResponse=MutableLiveData<String>()
    val patientType=MutableLiveData<ChoosePatient>()
    val offerId=MutableLiveData<Int>()
    val rating=MutableLiveData<RatingDoctors>()
    val polyClinicResponse=MutableLiveData<PolyClinicData>()
    val doctorClinksResponseResponse=MutableLiveData<GetDoctorClinksResponse>()
    val labsResponse=MutableLiveData<LabsInfo>()
    val feesResponse=MutableLiveData<Int>()
    val offerResponse=MutableLiveData<Offer>()
    val checkChooseCity=MutableLiveData<Boolean>(false)


    fun getFees(fees:Int){
        feesResponse.postValue(fees)
    }
    fun getspecialication(specialist:SpecialistData){
        specialication.postValue(specialist)
    }
    fun getOfferInfo(offer: Offer){
        offerResponse.postValue(offer)
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


    fun getServiceLevelData(serviceLevel: ServiceLevelData){
        serviceLevelData.postValue(serviceLevel)
    }

    fun getCity(city:CityData){
        getCity.postValue(city)
    }

    fun getRelationType(relation: DataRelation){
        relationResponse.postValue(relation)
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
    fun getClinicIdAndDoctorId(clinicData: ClinicData){
        clinicIdAndDoctorId.postValue(clinicData)
    }

    fun getOfferId(offerid: Int?){
        offerId.postValue(offerid!!)
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
    fun CheckCity(status:Boolean){
        checkChooseCity.postValue(status)
    }

    fun getSkipLoginStatus(skip:String){
        skipLoginStatus.postValue(skip)
    }
    fun getNavigateStatus(navigateStatus:String){
        navigateStatusResponse.postValue(navigateStatus)
    }
    fun getRating(ratingDoctors: RatingDoctors){
        rating.postValue(ratingDoctors)
    }
    fun getPolyClinicsData(polyClinicData: PolyClinicData){
        polyClinicResponse.postValue(polyClinicData)
    }
    fun getDoctorClinksResponseResponse(doctorClinksResponse: GetDoctorClinksResponse){
        doctorClinksResponseResponse.postValue(doctorClinksResponse)
    }
    fun getLabsInfoResponse(labsInfo: LabsInfo){
        labsResponse.postValue(labsInfo)
    }

}