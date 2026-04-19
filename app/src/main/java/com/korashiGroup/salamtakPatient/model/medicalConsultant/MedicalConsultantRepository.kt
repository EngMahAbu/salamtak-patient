package com.korashiGroup.salamtakPatient.model.medicalConsultant

interface MedicalConsultantRepository {
    suspend fun sendMedicalConsultant(fullName: String,
                                      mobileNumber: String,
                                      specialityId: Int,
                                      symptoms: String,
                                      question: String): MedicalConsultantResponse
}