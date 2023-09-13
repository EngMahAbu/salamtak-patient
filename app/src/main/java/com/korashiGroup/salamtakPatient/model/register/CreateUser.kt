package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateUser(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("UserTypeId")
    val userTypeId: Int
): Serializable