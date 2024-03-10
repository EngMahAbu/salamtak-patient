package com.korashiGroup.salamtakPatient.model.docotorsearch


import com.google.gson.annotations.SerializedName

data class DoctorSearchRequest(
    @SerializedName("AreaId")
    val areaId: Int=0,
    @SerializedName("AvalibleDate")
    val avalibleDate: Any?=null,
    @SerializedName("CityId")
    val cityId: Int=0,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("FeesFrom")
    val feesFrom: Int=0,
    @SerializedName("FeesTo")
    val feesTo: Int=0,
    @SerializedName("GenderId")
    val genderId: Int=0,
    @SerializedName("MaxResultCount")
    val maxResultCount: Int=0,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int=0,
    @SerializedName("SeniortyLevelId")
    val seniortyLevelId: Int=0,
    @SerializedName("SkipCount")
    val skipCount: Int=0,
    @SerializedName("SpecialistId")
    val specialistId: Int=0,
    @SerializedName("SubSpecialistId")
    val subSpecialistId: List<Int?>?=null,
    @SerializedName("InsuranceID")
    val insuranceID: String="",
)