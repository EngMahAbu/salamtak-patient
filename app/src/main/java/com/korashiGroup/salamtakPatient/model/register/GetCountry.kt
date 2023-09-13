package com.korashiGroup.salamtakPatient.model.register


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetCountry(
    @SerializedName("Data")
    val `data`: List<DataCountry>
):Serializable

data class DataCountry(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Name")
    val name: String
): Serializable