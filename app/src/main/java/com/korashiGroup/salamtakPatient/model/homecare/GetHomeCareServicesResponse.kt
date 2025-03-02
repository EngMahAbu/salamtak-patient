package com.korashiGroup.salamtakPatient.model.homecare


import com.google.gson.annotations.SerializedName

data class GetHomeCareServicesResponse(
    @SerializedName("Data")
    val `data`: List<HomeCareData?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class HomeCareData(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Inactive")
    val inactive: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("SpecialityId")
    val specialityId: Int?
)