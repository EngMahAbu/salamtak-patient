package com.korashiGroup.salamtakPatient.model.notification


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoctorNotificationResponse(
    @SerializedName("Data")
    val `data`: Data?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
):Serializable
data class Data(
    @SerializedName("Responses")
    val responses: List<Response?>?,
    @SerializedName("UnreadCount")
    val unreadCount: Int?
):Serializable
data class Response(
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsNotified")
    val isNotified: Boolean?,
    @SerializedName("PatientSymptomId")
    val patientSymptomId: Int?,
    @SerializedName("ResponseText")
    val responseText: String?
):Serializable