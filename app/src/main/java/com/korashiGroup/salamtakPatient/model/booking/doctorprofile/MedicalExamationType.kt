package com.korashiGroup.salamtakPatient.model.booking.doctorprofile


import com.google.gson.annotations.SerializedName

data class MedicalExamationType(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Name")
    val name: String?
)