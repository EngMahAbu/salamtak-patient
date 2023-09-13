package com.korashiGroup.salamtakPatient.model.docotorsearch.doc


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("AvailableFrom")
    val availableFrom: Any?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicName")
    val clinicName: Any?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("FeesFrom")
    val feesFrom: Int?,
    @SerializedName("FeesTo")
    val feesTo: Int?,
    @SerializedName("HealthEntities")
    val healthEntities: List<HealthEntity?>?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("MedicalExamationTypes")
    val medicalExamationTypes: Any?,
    @SerializedName("NumVisites")
    val numVisites: Int?,
    @SerializedName("Rate")
    val rate: Int?,
    @SerializedName("SeniortyLevelName")
    val seniortyLevelName: String?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    @SerializedName("SumRate")
    val sumRate: Int?,
    @SerializedName("WaitingTime")
    val waitingTime: Int?
)