package com.korashiGroup.salamtakPatient.model.labsandscanner


import com.google.gson.annotations.SerializedName

data class LabAppointementResponse(
    @SerializedName("Data")
    val `data`: List<LabDataAppointement?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class LabDataAppointement(
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("BranchAddress")
    val branchAddress: String?,
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("BranchImageUrl")
    val branchImageUrl: String?,
    @SerializedName("LabName")
    val labName: String?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("Services")
    val services: List<LabService?>?,
    @SerializedName("Status")
    val status: Int?
)
data class LabService(
    @SerializedName("ServiceCost")
    val serviceCost: Int?,
    @SerializedName("ServiceId")
    val serviceId: Int?,
    @SerializedName("ServiceName")
    val serviceName: String?
)