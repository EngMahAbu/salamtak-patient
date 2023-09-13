package com.korashiGroup.salamtakPatient.model.booking

import com.google.gson.annotations.SerializedName

data class CreatepatientAppointementsResponse(
    @SerializedName("Data")
    val data: Data?,
    @SerializedName("Message")
    val message: String,
)
data class Data(
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("doctorDto")
    val doctorDto: AppointementDoctorDto?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("DoctorWorkingDayTimeId")
    val doctorWorkingDayTimeId: Int?,
    @SerializedName("DurationMedicalExaminationId")
    val durationMedicalExaminationId: Any?,
    @SerializedName("Fees")
    val fees: Int?,
    @SerializedName("HealthEntityPhoneDtos")
    val healthEntityPhoneDtos: Any?,
    @SerializedName("HealthentityAddress")
    val healthentityAddress: Any?,
    @SerializedName("HealthentityName")
    val healthentityName: Any?,
    @SerializedName("IsBook")
    val isBook: Boolean?,
    @SerializedName("MaxNoOfPatients")
    val maxNoOfPatients: Any?,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String?,
    @SerializedName("PatientName")
    val patientName: String?,
    @SerializedName("PatientNumber")
    val patientNumber: String?,
    @SerializedName("TimeInterval")
    val timeInterval: Any?
)
data class AppointementDoctorDto(
    @SerializedName("Birthday")
    val birthday: String?,
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
    @SerializedName("LastNotification")
    val lastNotification: String?,
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
    val userId: Int?
)