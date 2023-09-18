package com.korashiGroup.salamtakPatient.model.insurance


import com.google.gson.annotations.SerializedName

data class SendInsuranceCardResponce(
    @SerializedName("Data")
    val `data`: DataCard,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class DataCard(
    @SerializedName("EndDate")
    val endDate: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("ImageURL")
    val imageURL: String,
    @SerializedName("InsCompanyID")
    val insCompanyID: Any,
    @SerializedName("InsCompanyName")
    val insCompanyName: Any,
    @SerializedName("IssueDate")
    val issueDate: String,
    @SerializedName("PatientID")
    val patientID: Int,
    @SerializedName("PatientName")
    val patientName: Any
)