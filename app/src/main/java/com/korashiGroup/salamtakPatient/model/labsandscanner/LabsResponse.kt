package com.korashiGroup.salamtakPatient.model.labsandscanner


import com.google.gson.annotations.SerializedName

data class LabsResponse(
    @SerializedName("Data")
    val `data`: List<DataLabs?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class DataLabs(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Name")
    val name: String?
)