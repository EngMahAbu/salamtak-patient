package com.korashiGroup.salamtakPatient.model.mobileClinic

data class MobileClinicReqBody(
    val FullName: String,
    val MobailPhone: String,
    val BuildingNumber: String,
    val Floor: String,
    val Aparment: String,
    val Street: String,
    val CityId: Int,
    val AreaId: Int,
    val SpecialityId: Int,
    val SubSpecialistyId: Int,
    val SeniorityLevelId: Int,
   // val Describtion: String,
    val SepcialRecuirements: String,
    val GenderId: Int,
    val Symptoms: String

)
