package com.korashiGroup.salamtakPatient.repository

import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.model.queue.PatientQueueStatusResponse
import retrofit2.Response
import javax.inject.Inject

class QueueRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMyQueueStatus(
        culture: String,
        patientAppointmentId: Int
    ): Response<PatientQueueStatusResponse> {
        return apiService.getMyQueueStatus(culture, patientAppointmentId)
    }

    suspend fun createTicketFromAppointment(
        culture: String,
        patientAppointmentId: Int
    ): Response<PatientQueueStatusResponse> {
        return apiService.createTicketFromAppointment(culture, patientAppointmentId)
    }
}