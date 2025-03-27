package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class ActiveOffersResponse(
    @SerializedName("Data")
    val data: List<ActiveOffers>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)
data class ActiveOffers(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?=null,
    val imageRes: Int? = null,
    @SerializedName("Name")
    val name: String,
    @SerializedName("OfferCount")
    val offerCount: Int
)