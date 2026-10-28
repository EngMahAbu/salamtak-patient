package com.korashiGroup.salamtakPatient.model.hospital

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HospitalDoctorResponse(
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Data")
    val data: List<HospitalDoctor>?,
    @SerializedName("Success")
    val success: Boolean?
) : Serializable

data class HospitalDoctor(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("FullNameAr")
    val fullNameAr: String?,
    @SerializedName("GenderName")
    val genderName: String?,
    @SerializedName("SpecialistId")
    val specialistId: Int?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String>?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Birthday")
    val birthday: String?,
    @SerializedName("UserId")
    val userId: Int?,
    @SerializedName("ClinicID")
    val clinicId: Int?,
    @SerializedName("RateCount")
    val rateCount: Int?,
    @SerializedName("Rate")
    val rate: Double?,
    @SerializedName("StatusId")
    val statusId: Int?,
    @SerializedName("Fees")
    val fees: Double?,
    @SerializedName("StatusName")
    val statusName: String?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("HealthEntityName")
    val healthEntityName: String?,
    @SerializedName("HealthEntityNameAr")
    val healthEntityNameAr: String?,
    @SerializedName("MedicalExamationTypes")
    val medicalExamationTypes: List<MedicalExamationType>?
) : Serializable

data class MedicalExamationType(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Image")
    val image: String?
) : Serializable
