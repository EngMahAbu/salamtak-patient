package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class PatientMedicalInfoResponse(
    @SerializedName("Data")
    val data: DataMedicalInfo?,
    @SerializedName("Message")
    val message: String?
)

data class DataMedicalInfo(
    @SerializedName("Statues")
    val Statues: Boolean?
)