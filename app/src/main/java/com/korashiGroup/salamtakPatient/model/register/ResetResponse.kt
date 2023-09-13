package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class ResetResponse(
    @SerializedName("Data")
    val data: ResetData?,
    @SerializedName("Message")
    val message: String?
)

data class ResetData(
    @SerializedName("Code")
    val code: Int?,
    @SerializedName("ReSendCounter")
    val reSendCounter: Int?,
    @SerializedName("UserId")
    val userId: Int?
)