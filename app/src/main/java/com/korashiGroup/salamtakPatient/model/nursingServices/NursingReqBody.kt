package com.korashiGroup.salamtakPatient.model.nursingServices

import com.google.gson.annotations.SerializedName

data class NursingReqBody(

    @SerializedName("FullName")
    val FullName: String,

    @SerializedName("MobailPhone")
    val MobailPhone: String,

    @SerializedName("BuildingNumber")
    val BuildingNumber: String,

    @SerializedName("Street")
    val Street: String,

    @SerializedName("Floor")
    val Floor: String,

    @SerializedName("Apartment")
    val Apartment: String,

    @SerializedName("CityId")
    val CityId: Int,

    @SerializedName("AreaId")
    val AreaId: Int,

    @SerializedName("GenderId")
    val GenderId: Int,

    @SerializedName("FromDay")
    val FromDay: String,

    @SerializedName("ToDay")
    val ToDay: String,

    @SerializedName("FromHour")
    val FromHour: String,

    @SerializedName("ToHour")
    val ToHour: String,

    @SerializedName("ServiceId")
    val ServiceId: Int,

    @SerializedName("Service")
    val Service: String
)
