package com.korashiGroup.salamtakPatient.model.queue

import com.google.gson.annotations.SerializedName

data class PatientQueueStatusResponse(
    @SerializedName("Data", alternate = ["data"])
    val data: PatientQueueStatusData?,
    @SerializedName("Message", alternate = ["message"])
    val message: String?,
    @SerializedName("IsSuccess", alternate = ["isSuccess", "success", "Success"])
    val isSuccess: Boolean?
)

data class PatientQueueStatusData(
    @SerializedName("TicketId", alternate = ["ticketId"])
    val ticketId: Int?,
    @SerializedName("TicketPosition", alternate = ["ticketPosition"])
    val ticketPosition: Int?,
    @SerializedName("PatientName", alternate = ["patientName"])
    val patientName: String?,
    @SerializedName("Status", alternate = ["status"])
    val status: Int?,
    @SerializedName("StatusName", alternate = ["statusName"])
    val statusName: String?,
    @SerializedName("IsWalkIn", alternate = ["isWalkIn"])
    val isWalkIn: Boolean?,
    @SerializedName("OriginalBookingTime", alternate = ["originalBookingTime"])
    val originalBookingTime: String?,
    @SerializedName("CheckInTime", alternate = ["checkInTime"])
    val checkInTime: String?,
    @SerializedName("ServiceStartTime", alternate = ["serviceStartTime"])
    val serviceStartTime: String?,
    @SerializedName("DoctorName", alternate = ["doctorName"])
    val doctorName: String?,
    @SerializedName("DepartmentName", alternate = ["departmentName"])
    val departmentName: String?,
    @SerializedName("CurrentlyServingPosition", alternate = ["currentlyServingPosition"])
    val currentlyServingPosition: Int?,
    @SerializedName("PeopleAhead", alternate = ["peopleAhead"])
    val peopleAhead: Int?,
    @SerializedName("EstimatedWaitMinutes", alternate = ["estimatedWaitMinutes"])
    val estimatedWaitMinutes: Int?
)
