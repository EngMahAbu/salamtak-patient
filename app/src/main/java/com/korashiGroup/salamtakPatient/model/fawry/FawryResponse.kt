package com.korashiGroup.salamtakPatient.model.fawry


import com.google.gson.annotations.SerializedName

data class FawryResponse(
    @SerializedName("Data")
    val data: DataFawry,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)
data class DataFawry(
    @SerializedName("merchantCode")
    val merchantCode: Any,
    @SerializedName("merchantSecretCode")
    val merchantSecretCode: Any,
    @SerializedName("URL")
    val uRL: Any
)