package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class RegisterReponse(
    @SerializedName("Data")
    val `data`: Data,
    @SerializedName("Message")
    val message: String,
): Serializable

data class Data(
    @SerializedName("Code")
    val code: Int,
    @SerializedName("ReSendCounter")
    val reSendCounter: Int,
    @SerializedName("UserId")
    val userId: Int
):Serializable