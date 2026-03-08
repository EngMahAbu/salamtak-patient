package com.korashiGroup.salamtakPatient.model.whatspresponse


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WhatspRequest(
    @SerializedName("PhoneNumber")
    val phone: String,
): Serializable