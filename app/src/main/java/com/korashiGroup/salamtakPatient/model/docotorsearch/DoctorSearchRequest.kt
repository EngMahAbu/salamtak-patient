package com.korashiGroup.salamtakPatient.model.docotorsearch


import com.google.gson.annotations.SerializedName

data class DoctorSearchRequest(
    @SerializedName("AreaId")
    val areaId: Int?=null,
    @SerializedName("AvalibleDate")
    val avalibleDate: Any?=null,
    @SerializedName("CityId")
    val CityId: Int=0,
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
    val insuranceID: String?=null,
){
    constructor(
        CityId: Int,
        DoctorName: String,
        MaxResultCount: Int,
        MedicalExaminationTypeId: Int,
        SkipCount: Int,
        SpecialistId: Int,
    ) : this(0,null,CityId,"",0,0,0,0,0,0,0,0,null,null)
}