package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class PatientProfileResponse(
    @SerializedName("Data")
    val data: Data?,
    @SerializedName("Message")
    val message: String
)

data class Data(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?
)