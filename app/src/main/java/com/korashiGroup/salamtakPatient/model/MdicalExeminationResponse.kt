package com.korashiGroup.salamtakPatient.model


import com.google.gson.annotations.SerializedName

data class MedicalExaminationResponse(
    @SerializedName("Data")
    var data: List<MedicalExamination?>?
)

data class MedicalExamination(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Inactive")
    val inactive: Boolean?,
    @SerializedName("Name")
    val name: String?
)