package com.korashiGroup.salamtakPatient.model.scheduling


import com.google.gson.annotations.SerializedName

data class GetPatientAppointmentesResponse(
    @SerializedName("Data")
    val data: List<DataAppointment?>?,
    @SerializedName("Message")
    val message: String?
)

data class DataAppointment(
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("CanRate")
    val canRate: Boolean?,
    @SerializedName("ClinicAddress")
    val clinicAddress: String?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicLatitude")
    val clinicLatitude: String?,
    @SerializedName("ClinicLongitude")
    val clinicLongitude: String?,
    @SerializedName("ClinicName")
    val clinicName: String?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorImage")
    val doctorImage: String?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("Fees")
    val fees: Int?,
    @SerializedName("IsCancel")
    val isCancel: Boolean?,
    @SerializedName("IsSeen")
    val isSeen: Boolean?,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int?,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    @SerializedName("IsCompleted")
    val IsCompleted: Boolean?,

)