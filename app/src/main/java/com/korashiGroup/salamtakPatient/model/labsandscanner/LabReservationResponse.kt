package com.korashiGroup.salamtakPatient.model.labsandscanner


import com.google.gson.annotations.SerializedName

data class LabReservationResponse(
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
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("Services")
    val services: List<Service?>?,
    @SerializedName("Status")
    val status: Int?
)
data class Service(
    @SerializedName("ServiceCost")
    val serviceCost: Double?,
    @SerializedName("ServiceId")
    val serviceId: Int?,
    @SerializedName("ServiceName")
    val serviceName: String?
)