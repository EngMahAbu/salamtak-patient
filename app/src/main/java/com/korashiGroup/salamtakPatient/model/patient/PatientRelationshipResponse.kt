package com.korashiGroup.salamtakPatient.model.patient


import com.google.gson.annotations.SerializedName

data class PatientRelationshipResponse(
    @SerializedName("Data")
    val relation: List<DataRelation?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)

data class DataRelation(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)