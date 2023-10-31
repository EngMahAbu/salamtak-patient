package com.korashiGroup.salamtakPatient.model.reviews


import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("Data")
    val data: List<ReviewsData>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class ReviewsData(
    @SerializedName("Comment")
    val comment: String,
    @SerializedName("PatientImage")
    val patientImage: Any,
    @SerializedName("PatientName")
    val patientName: String,
    @SerializedName("Rate")
    val rate: Int
)