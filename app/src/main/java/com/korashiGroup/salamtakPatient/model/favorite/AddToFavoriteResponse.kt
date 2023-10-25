package com.korashiGroup.salamtakPatient.model.favorite


import com.google.gson.annotations.SerializedName

data class AddToFavoriteResponse(
    @SerializedName("Data")
    val `data`: Any,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)