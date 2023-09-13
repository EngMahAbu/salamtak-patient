package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName

data class ResetRequest(
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("ResetMethod")
    val resetMethod: Int?,
    @SerializedName("UserTypeId")
    val userTypeId: Int?
)