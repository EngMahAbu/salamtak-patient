package com.korashiGroup.salamtakPatient.notification


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("link")
    val link: String?
)