package com.korashiGroup.salamtakPatient.model.populardoctors

import com.google.gson.annotations.SerializedName

data class PopularDoctorsResponseItem (
    @SerializedName("DoctorImage")
    val doctorImage: String?,
    @SerializedName("DoctorNameAr")
    val doctorNameAr: String?,
    @SerializedName("DoctorNameEn")
    val doctorNameEn: String?,
    @SerializedName("DoctorSpec")
    val doctorSpec: String?,
    @SerializedName("Rate")
    val rate: Double?,
    @SerializedName("DoctorId")
    val DoctorId: Int?
)