package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BloodTypeResponse(
    @SerializedName("Data")
    val data: List<DataBloodType?>?,
    @SerializedName("Message")
    val message: String?
): Serializable

data class DataBloodType(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
): Serializable