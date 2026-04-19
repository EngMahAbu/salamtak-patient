package com.korashiGroup.salamtakPatient.model.medicalConsultant

data class MedicalConsultantReqBody(
    val FullName: String,
    val MobileNumber: String,
    val SpecialityId: Int,
    val Symptoms: String,
    val Question: String
)