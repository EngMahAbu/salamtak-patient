package com.korashiGroup.salamtakPatient.model.scheduling


import com.google.gson.annotations.SerializedName

data class CancelPatientAppointmentResponse(
    @SerializedName("Data")
    val `data`: Any,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)
data class Data(
    @SerializedName("Statues")
    val statues: Boolean?
)