package com.korashiGroup.salamtakPatient.model.docotorsearch


import com.google.gson.annotations.SerializedName

data class DoctorSearchRequest(
    @SerializedName("AvalibleDate")
    val avalibleDate: Any?="",
    @SerializedName("CityId")
    val CityId: Int?=null,
    @SerializedName("AreaId")
    val AreaId: Int?=null,
    @SerializedName("DoctorName")
    val doctorName: String?=null,
    @SerializedName("FeesFrom")
    val feesFrom: Int?=null,
    @SerializedName("FeesTo")
    val feesTo: Int?=null,
    @SerializedName("GenderId")
    val genderId: Int?=null,
    @SerializedName("MaxResultCount")
    val maxResultCount: Int=0,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int=0,
    @SerializedName("SeniortyLevelId")
    val seniortyLevelId: Int?=null,
    @SerializedName("SkipCount")
    val skipCount: Int=0,
    @SerializedName("SpecialistId")
    val specialistId: Int?=null,
    @SerializedName("SubSpecialistId")
    val subSpecialistId: List<Int?>?=null,
    @SerializedName("InsuranceID")
    val insuranceID: String?=null,
)