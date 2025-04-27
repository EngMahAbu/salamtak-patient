package com.korashiGroup.salamtakPatient.model.booking.doctorprofile


import com.google.gson.annotations.SerializedName

data class DoctorProfileResponse(
    @SerializedName("Data")
    val `data`: Data?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)