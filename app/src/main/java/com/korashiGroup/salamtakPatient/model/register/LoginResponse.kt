package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse(
    @SerializedName("Data")
    val data: DataLogin,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
): Serializable

data class DataLogin(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: Any,
    @SerializedName("Name")
    val name: String,
    @SerializedName("NameAR")
    val NameAR: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("ProfileStatus")
    val profileStatus: Int,
    @SerializedName("RoleID")
    val roleID: Int,
    @SerializedName("Token")
    val token: String
):Serializable
