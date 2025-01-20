package com.korashiGroup.salamtakPatient.model.labsandscanner


import com.google.gson.annotations.SerializedName

data class AnalysisResponse(
    @SerializedName("Data")
    val `data`: List<DataAnalysis?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class DataAnalysis(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)