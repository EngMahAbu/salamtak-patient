package com.korashiGroup.salamtakPatient.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetSpecialistResponse(
    @SerializedName("Data")
    val `data`: List<SpecialistData>
): Serializable

data class SpecialistData(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: String,
    @SerializedName("inactive")
    val inactive: Boolean,
    @SerializedName("Name")
    val name: String
): Serializable