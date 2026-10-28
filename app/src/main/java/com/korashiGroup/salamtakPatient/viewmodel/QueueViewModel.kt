package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.queue.PatientQueueStatusResponse
import com.korashiGroup.salamtakPatient.repository.QueueRepository
import com.korashiGroup.salamtakPatient.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val repository: QueueRepository
) : ViewModel() {

    private val _queueStatus = MutableLiveData<Resource<PatientQueueStatusResponse>>()
    val queueStatus: LiveData<Resource<PatientQueueStatusResponse>> = _queueStatus

    fun getMyQueueStatus(culture: String, appointmentId: Int) {
        _queueStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                var response = repository.getMyQueueStatus(culture, appointmentId)
                val body = response.body()
                val needsTicketCreation = !response.isSuccessful || body?.data == null || (body.data.ticketPosition == null && body.data.statusName.isNullOrBlank())

                if (needsTicketCreation) {
                    try {
                        val createTicketResponse = repository.createTicketFromAppointment(culture, appointmentId)
                        if (createTicketResponse.isSuccessful && createTicketResponse.body()?.data != null) {
                            val refreshed = repository.getMyQueueStatus(culture, appointmentId)
                            if (refreshed.isSuccessful && refreshed.body()?.data != null) {
                                response = refreshed
                            } else if (createTicketResponse.body() != null) {
                                _queueStatus.value = Resource.sucess(createTicketResponse.body()!!)
                                return@launch
                            }
                        }
                    } catch (_: Exception) {
                        // Ignore create ticket error and proceed with original getMyQueueStatus handling
                    }
                }

                _queueStatus.value = handleResponse(response)
            } catch (e: Exception) {
                _queueStatus.value = Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    private fun handleResponse(response: Response<PatientQueueStatusResponse>): Resource<PatientQueueStatusResponse> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Resource.sucess(body)
            }
        }
        val errorMsg = try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val parsed = Gson().fromJson(errorBody, PatientQueueStatusResponse::class.java)
                parsed?.message ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
        val finalMsg = if (errorMsg.isNotBlank()) errorMsg else (response.message() ?: "")
        return Resource.Error(if (finalMsg.isNotBlank()) finalMsg else "No queue ticket found")
    }
}