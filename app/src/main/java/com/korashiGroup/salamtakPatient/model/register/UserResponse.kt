package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("Data")
    val data: User,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class User(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: Any,
    @SerializedName("Name")
    val name: String,
    @SerializedName("NameAR")
    val nameAR: Any,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("ProfileStatus")
    val profileStatus: Int,
    @SerializedName("RoleID")
    val roleID: Any,
    @SerializedName("Token")
    val token: String
)