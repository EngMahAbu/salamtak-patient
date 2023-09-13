package com.korashiGroup.salamtakPatient.model.booking


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookingResponse(
    @SerializedName("Data")
    val data: List<BookingData?>?
): Serializable

data class BookingData(
    @SerializedName("BookedAppointments")
    val bookedAppointments: List<String?>?,
    @SerializedName("DayId")
    val dayId: Int?,
    @SerializedName("DayName")
    val dayName: String?,
    @SerializedName("DurationMedicalExaminationId")
    val durationMedicalExaminationId: Int?,
    @SerializedName("Fees")
    val fees: Int?,
    @SerializedName("FollowUp_Fees")
    val followUpFees: Any?,
    @SerializedName("Inactive")
    val inactive: Boolean?,
    @SerializedName("MaxNoOfPatients")
    val maxNoOfPatients: Int?,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int?,
    @SerializedName("MedicalExaminationTypeImage")
    val medicalExaminationTypeImage: String?,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String?,
    @SerializedName("OverlapApproved")
    val overlapApproved: Boolean?,
    @SerializedName("SchedualId")
    val schedualId: Int?,
    @SerializedName("TimeFrom")
    val timeFrom: String?,
    @SerializedName("TimeInterval")
    val timeInterval: Int?,
    @SerializedName("TimeTo")
    val timeTo: String?
):Serializable