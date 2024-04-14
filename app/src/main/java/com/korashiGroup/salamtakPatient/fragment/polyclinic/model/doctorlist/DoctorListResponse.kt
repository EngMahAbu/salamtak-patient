package com.korashiGroup.salamtakPatient.fragment.polyclinic.model.doctorlist


import com.google.gson.annotations.SerializedName

data class DoctorListResponse(
    @SerializedName("Data")
    val `data`: Data,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)