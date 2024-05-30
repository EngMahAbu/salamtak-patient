package com.korashiGroup.salamtakPatient.fragment.polyclinic.model


import com.google.gson.annotations.SerializedName

data class PolyClinicsDoctorsResponse(
    @SerializedName("Data")
    val `data`: List<PolyClinicsDoctorsList?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class PolyClinicsDoctorsList(
    @SerializedName("Birthday")
    val birthday: String?,
    @SerializedName("ChangeDate")
    val changeDate: String?,
    @SerializedName("CreatedByName")
    val createdByName: Any?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("FullNameAr")
    val fullNameAr: String?,
    @SerializedName("GenderName")
    val genderName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("IsSeen")
    val isSeen: Any?,
    @SerializedName("LastNotification")
    val lastNotification: String?,
    @SerializedName("NickName")
    val nickName: String?,
    @SerializedName("NickNameAr")
    val nickNameAr: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("StatusId")
    val statusId: Int?,
    @SerializedName("StatusName")
    val statusName: String?,
    @SerializedName("UserId")
    val userId: Int?,
    val Fees: Int?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    val RateCount: Int?,
    @SerializedName("Rate")
    val rate: Double?,
    val ClinicID: Int?,
    @SerializedName("CityName")
    val CityName: String?,
    @SerializedName("AreaName")
    val AreaName: String?,
)