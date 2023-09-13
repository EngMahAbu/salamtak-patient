package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class MedicineAllergyResponse(
    @SerializedName("Data")
    val data: List<DataMedicineAllergy?>?,
    @SerializedName("Message")
    val message: String?
)

data class DataMedicineAllergy(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
)