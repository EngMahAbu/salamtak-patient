package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(
    @SerializedName("Data")
    val data: UpdateData?,
    @SerializedName("Message")
    val message: String?
)

data class UpdateData(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: Any?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("NameAR")
    val nameAR: Any?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?,
    @SerializedName("RoleID")
    val roleID: Any?,
    @SerializedName("Token")
    val token: Any?
)