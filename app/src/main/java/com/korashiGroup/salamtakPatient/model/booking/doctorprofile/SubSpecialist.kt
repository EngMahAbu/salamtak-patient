package com.korashiGroup.salamtakPatient.model.booking.doctorprofile


import com.google.gson.annotations.SerializedName

data class SubSpecialist(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)