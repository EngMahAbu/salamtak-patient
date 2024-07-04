package com.korashiGroup.salamtakPatient.model.checktoken


import com.google.gson.annotations.SerializedName

data class CheckTokenResponse(
    @SerializedName("Statues")
    val statues: Boolean?
)