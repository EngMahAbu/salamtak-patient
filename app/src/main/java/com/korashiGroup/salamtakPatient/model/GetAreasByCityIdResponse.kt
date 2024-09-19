package com.korashiGroup.salamtakPatient.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetAreasByCityIdResponse(
    @SerializedName("Data")
    val `data`: List<AreaData>
): Serializable

data class AreaData(
    @SerializedName("Id")
    val id: Int?=1,
    @SerializedName("Name")
    val name: String
):Serializable