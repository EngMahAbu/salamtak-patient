package com.korashiGroup.salamtakPatient.fragment.polyclinic.model


import com.google.gson.annotations.SerializedName

data class PolyClincResponse(
    @SerializedName("Data")
    val `data`: Data,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)