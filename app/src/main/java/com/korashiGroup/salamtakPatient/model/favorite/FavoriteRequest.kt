package com.korashiGroup.salamtakPatient.model.favorite


import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
    @SerializedName("DoctorId")
    val doctorId: Int,
    @SerializedName("IsFavoirt")
    val isFavoirt: Boolean
)