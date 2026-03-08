package com.korashiGroup.salamtakPatient.model.whatspresponse


import com.google.gson.annotations.SerializedName

data class WhatSpResponse(
    @SerializedName("Message")
    val message: String?,
    @SerializedName("OtpCode")
    val otpCode: String?,
    @SerializedName("Success")
    val success: Boolean?
)