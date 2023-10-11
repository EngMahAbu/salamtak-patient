package com.korashiGroup.salamtakPatient.model.faweryjson


import com.google.gson.annotations.SerializedName

data class Fawery(
    @SerializedName("expirationTime")
    val expirationTime: String,
    @SerializedName("fawryFees")
    val fawryFees: Double,
    @SerializedName("orderAmount")
    val orderAmount: String,
    @SerializedName("orderStatus")
    val orderStatus: String,
    @SerializedName("paymentAmount")
    val paymentAmount: String,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("referenceNumber")
    val referenceNumber: String?=null,
    @SerializedName("shippingFees")
    val shippingFees: Double,
    @SerializedName("taxes")
    val taxes: Double,
    @SerializedName("signature")
    val signature:String?=null
)