package com.korashiGroup.salamtakPatient.model.populardoctors


import com.google.gson.annotations.SerializedName

data class GetDoctorSpotLightResponse(
    @SerializedName("Data")
    val data: List<SpotlightData?>?,
    @SerializedName("Message")
    val message: String?
)

data class SpotlightData(
    @SerializedName("CreatedDate")
    val createdDate: String?,
    @SerializedName("Details")
    val details: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("ImageURL")
    val imageURL: String?,
    @SerializedName("IsHighlight")
    val isHighlight: Boolean?,
    @SerializedName("Title")
    val title: String?
)