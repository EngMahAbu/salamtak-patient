package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateUser(
    @SerializedName("Email")
    val email: String?=null,
    @SerializedName("Name")
    val name: String?=null,
    @SerializedName("Password")
    val password: String?=null,
    @SerializedName("Phone")
    val phone: String?=null,
    @SerializedName("UserTypeId")
    val userTypeId: Int?=null
): Serializable