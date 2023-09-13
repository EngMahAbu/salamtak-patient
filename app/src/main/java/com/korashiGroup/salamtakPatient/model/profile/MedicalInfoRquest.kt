package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class MedicalInfoRquest(
    @SerializedName("BloodTypeId")
    val bloodTypeId: Int?,
    @SerializedName("ChronicDiseases")
    val chronicDiseases: String?,
    @SerializedName("CurrentMedication")
    val currentMedication: String?,
    @SerializedName("Height")
    val height: Int?,
    @SerializedName("Iinjuries")
    val iinjuries: String?,
    @SerializedName("OtherAllergies")
    val otherAllergies: String?,
    @SerializedName("PastMedication")
    val pastMedication: String?,
    @SerializedName("PatientFoodAllergiesDto")
    val patientFoodAllergiesDto: List<Int?>?,
    @SerializedName("PatientMedicineAllergiesDto")
    val patientMedicineAllergiesDto: List<Int?>?,
    @SerializedName("Prescriptions")
    val prescriptions: String?,
    @SerializedName("Pressure")
    val pressure: String?,
    @SerializedName("SugarLevel")
    val sugarLevel: String?,
    @SerializedName("Surgeries")
    val surgeries: String?,
    @SerializedName("Weight")
    val weight: Int?
)