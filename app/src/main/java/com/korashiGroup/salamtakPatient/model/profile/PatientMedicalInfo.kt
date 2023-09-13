package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class PatientMedicalInfo(
    @SerializedName("Data")
    val `data`: DataPatientMedicalInfo?,
    @SerializedName("Message")
    val message: String?
)

data class DataPatientMedicalInfo(
    @SerializedName("BloodName")
    val bloodName: Any?,
    @SerializedName("BloodTypeId")
    val bloodTypeId: Any?,
    @SerializedName("ChronicDiseases")
    val chronicDiseases: Any?,
    @SerializedName("CurrentMedication")
    val currentMedication: Any?,
    @SerializedName("Height")
    val height: Int?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Iinjuries")
    val iinjuries: Any?,
    @SerializedName("OtherAllergies")
    val otherAllergies: Any?,
    @SerializedName("PastMedication")
    val pastMedication: Any?,
    @SerializedName("PatientFoodAllergiesDto")
    val patientFoodAllergiesDto: List<Int?>?,
    @SerializedName("PatientFoodAllergiesName")
    val patientFoodAllergiesName: List<String?>?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("PatientMedicineAllergiesDto")
    val patientMedicineAllergiesDto: List<Int?>?,
    @SerializedName("PatientMedicineAllergiesName")
    val patientMedicineAllergiesName: List<String?>?,
    @SerializedName("Prescriptions")
    val prescriptions: Any?,
    @SerializedName("Pressure")
    val pressure: Any?,
    @SerializedName("SugarLevel")
    val sugarLevel: Any?,
    @SerializedName("Surgeries")
    val surgeries: Any?,
    @SerializedName("Weight")
    val weight: Int?
)