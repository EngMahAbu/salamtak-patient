package com.korashiGroup.salamtakPatient.model.docotorsearch


import com.google.gson.annotations.SerializedName

data class NewDoctorSearchRequest(
    @SerializedName("CityId")
    val cityId: Int?=1,
    @SerializedName("AreaId")
    val AreaId: Int?=1,
    @SerializedName("MaxResultCount")
    val maxResultCount: Int=0,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int=0,
    @SerializedName("SkipCount")
    val skipCount: Int=0,
    @SerializedName("SpecialistId")
    val specialistId: Int=0,
)