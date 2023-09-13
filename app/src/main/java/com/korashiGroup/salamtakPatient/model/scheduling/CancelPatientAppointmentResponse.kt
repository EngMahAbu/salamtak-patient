package com.korashiGroup.salamtakPatient.model.scheduling


import com.google.gson.annotations.SerializedName

data class CancelPatientAppointmentResponse(
    @SerializedName("Data")
    val `data`: Data?,
    @SerializedName("Message")
    val message: String?
)
data class Data(
    @SerializedName("Statues")
    val statues: Boolean?
)