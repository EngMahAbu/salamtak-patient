package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("Data")
    val data: ChangeData?,
    @SerializedName("Message")
    val message: String?
)

data class ChangeData(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("NameAR")
    val nameAR: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?,
    @SerializedName("RoleID")
    val roleID: Any?,
    @SerializedName("Token")
    val token: String?
)