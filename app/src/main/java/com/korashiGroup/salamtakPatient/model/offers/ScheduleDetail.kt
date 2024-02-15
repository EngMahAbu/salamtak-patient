package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class ScheduleDetail(
    @SerializedName("BookedAppointments")
    val bookedAppointments: Any,
    @SerializedName("DayId")
    val dayId: Int,
    @SerializedName("DayName")
    val dayName: String,
    @SerializedName("DurationMedicalExaminationId")
    val durationMedicalExaminationId: Any,
    @SerializedName("Fees")
    val fees: Int,
    @SerializedName("FollowUp_Fees")
    val followUpFees: Int,
    @SerializedName("Inactive")
    val inactive: Boolean,
    @SerializedName("MaxNoOfPatients")
    val maxNoOfPatients: Int,
    @SerializedName("MedicalExamationTypeDto")
    val medicalExamationTypeDto: List<MedicalExamationTypeDto>,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int,
    @SerializedName("MedicalExaminationTypeImage")
    val medicalExaminationTypeImage: String,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String,
    @SerializedName("OverlapApproved")
    val overlapApproved: Boolean,
    @SerializedName("SchedualId")
    val schedualId: Int,
    @SerializedName("TimeFrom")
    val timeFrom: String,
    @SerializedName("TimeInterval")
    val timeInterval: Any,
    @SerializedName("TimeTo")
    val timeTo: String,
    @SerializedName("WorkingDate")
    val workingDate: Any
)