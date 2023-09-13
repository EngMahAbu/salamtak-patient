package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class UpdateProfilePatientResponse(
    @SerializedName("Data")
    val data: DataUpdated?,
    @SerializedName("Message")
    val message: String?
)

data class DataUpdated(
    @SerializedName("Statues")
    val Statues: Boolean?
)