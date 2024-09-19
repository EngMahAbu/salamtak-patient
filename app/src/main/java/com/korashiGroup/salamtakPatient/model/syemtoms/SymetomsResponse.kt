package com.korashiGroup.salamtakPatient.model.syemtoms


import com.google.gson.annotations.SerializedName

data class SymetomsResponse(
    @SerializedName("Data")
    val `data`: List<SyemtomsData?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class SyemtomsData(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)