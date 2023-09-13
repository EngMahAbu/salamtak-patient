package com.korashiGroup.salamtakPatient.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetSubSpecilistResponse(
    @SerializedName("Data")
    val `data`: List<SubSpecialistData>
):Serializable

data class SubSpecialistData(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: Any,
    @SerializedName("inactive")
    val inactive: Boolean,
    @SerializedName("Name")
    val name: String
):Serializable