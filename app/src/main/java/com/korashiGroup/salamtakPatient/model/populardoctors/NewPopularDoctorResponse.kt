package com.korashiGroup.salamtakPatient.model.populardoctors


import com.google.gson.annotations.SerializedName

data class NewPopularDoctorResponse(
    @SerializedName("Data")
    val data: MutableList<PopularDoctorS?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class PopularDoctorS(
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorImage")
    val doctorImage: String?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("DoctorNameAr")
    val doctorNameAr: String?,
    @SerializedName("DoctorNameEn")
    val doctorNameEn: String?,
    @SerializedName("DoctorSpec")
    val doctorSpec: String?,
    @SerializedName("DoctorSpecAr")
    val doctorSpecAr: String?,
    @SerializedName("DoctorSpeciality")
    val doctorSpeciality: String?,
    @SerializedName("Rate")
    val rate: Double?
)