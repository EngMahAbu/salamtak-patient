package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class MedicalExamationTypeDto(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: Any,
    @SerializedName("Name")
    val name: String
)