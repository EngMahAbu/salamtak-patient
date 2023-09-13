package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("OldPassword")
    val oldPassword: String?,
    @SerializedName("Password")
    val password: String?,
    @SerializedName("UserId")
    val userId: Int?
)