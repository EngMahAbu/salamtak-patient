package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class DoctorDto(
    @SerializedName("Birthday")
    val birthday: String,
    @SerializedName("CreatedByName")
    val createdByName: Any,
    @SerializedName("Email")
    val email: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("FullNameAr")
    val fullNameAr: String,
    @SerializedName("GenderName")
    val genderName: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: String,
    @SerializedName("IsSeen")
    val isSeen: Any,
    @SerializedName("LastNotification")
    val lastNotification: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String,
    @SerializedName("SpecialistName")
    val specialistName: String,
    @SerializedName("StatusId")
    val statusId: Int,
    @SerializedName("StatusName")
    val statusName: String,
    @SerializedName("UserId")
    val userId: Int
)