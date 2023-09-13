package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class FoodAllergyResponse(
    @SerializedName("Data")
    val data: List<DataFoodAllergy?>?,
    @SerializedName("Message")
    val message: String?
)
data class DataFoodAllergy(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)