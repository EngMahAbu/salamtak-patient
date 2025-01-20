package com.korashiGroup.salamtakPatient.model.labsandscanner


import com.google.gson.annotations.SerializedName

data class CalenderResponse(
    @SerializedName("Data")
    val `data`: List<CalenderData?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class CalenderData(
    @SerializedName("DayName")
    val dayName: String?,
    @SerializedName("EndTime")
    val endTime: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("StartTime")
    val startTime: String?
)