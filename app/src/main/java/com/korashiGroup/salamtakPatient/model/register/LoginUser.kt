package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class LoginUser(
    @SerializedName("FCM")
    val fCM: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("UserTypeId")
    val userTypeId: Int
)