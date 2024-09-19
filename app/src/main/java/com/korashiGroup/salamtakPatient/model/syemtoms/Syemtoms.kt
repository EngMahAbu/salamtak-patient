package com.korashiGroup.salamtakPatient.model.syemtoms


import com.google.gson.annotations.SerializedName

data class Syemtoms(
    @SerializedName("Data")
    val `data`: Data?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class Data(
    @SerializedName("Age")
    val age: Int?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("Comment")
    val comment: String?,
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("Symptoms")
    val symptoms: List<Symptom?>?
)
data class Symptom(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)