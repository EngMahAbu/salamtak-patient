package com.korashiGroup.salamtakPatient.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetSeniorityLevelResponse(
    @SerializedName("Data")
    val data: List<SeniorityLevelData>
): Serializable

data class SeniorityLevelData(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Name")
    val name: String
): Serializable