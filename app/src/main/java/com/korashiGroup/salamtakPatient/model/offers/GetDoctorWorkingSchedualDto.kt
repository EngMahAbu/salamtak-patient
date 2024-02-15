package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class GetDoctorWorkingSchedualDto(
    @SerializedName("Active")
    val active: Boolean,
    @SerializedName("DayId")
    val dayId: Int,
    @SerializedName("DayName")
    val dayName: String,
    @SerializedName("DayPeriodCount")
    val dayPeriodCount: Int,
    @SerializedName("FixedFee")
    val fixedFee: Int,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Any,
    @SerializedName("ScheduleDetails")
    val scheduleDetails: List<ScheduleDetail>,
    @SerializedName("WorkingDate")
    val workingDate: Any
)