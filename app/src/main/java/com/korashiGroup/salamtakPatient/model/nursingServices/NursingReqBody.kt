package com.korashiGroup.salamtakPatient.model.nursingServices

data class NursingReqBody(
    val FullName: String,
    val MobailPhone: String,
    val BuildingNumber: String,
    val Street: String,
    val Floor: String,
    val Apartment: String,
    val CityId: Int,
    val AreaId: Int,
    val FromDay: String,
    val ToDay: String,
    val FromHour: String,
    val ToHour: String,
    val GenderId: Int,
    val ServiceId: Int,
    val Service: String
)
