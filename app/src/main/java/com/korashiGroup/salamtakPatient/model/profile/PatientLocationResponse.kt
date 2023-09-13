package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class PatientLocationResponse(
    @SerializedName("Data")
    val data: DataLocation?,
    @SerializedName("Message")
    val message: String?
)

data class DataLocation(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?
)