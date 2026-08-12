package com.korashiGroup.salamtakPatient.model.populardoctors


import com.google.gson.annotations.SerializedName
import com.korashiGroup.salamtakPatient.model.docotorsearch.ClinicDto

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
    @SerializedName("DoctorSpec")
    val doctorSpec: String?,
    @SerializedName("DoctorSpecAr")
    val doctorSpecAr: String?,
    @SerializedName("DoctorSpeciality")
    val doctorSpeciality: String?,
    @SerializedName("clinicDto")
    val clinicDto: ClinicDto?,
    @SerializedName("Rate")
    val rate: Double?,
    @SerializedName("FeesTo")
    val feesTo: Int?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("WaitingTime")
    val waitingTime: Int?,
    @SerializedName("Sub_SpecialistList")
    val subSpecialistName: List<SubSpecial?>?,
    val RateCount: Int?,
    val NickName: String?,
    )

data class SubSpecial(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?,
)