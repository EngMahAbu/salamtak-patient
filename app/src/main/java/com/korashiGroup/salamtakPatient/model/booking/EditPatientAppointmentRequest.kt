package com.korashiGroup.salamtakPatient.model.booking

import java.io.Serializable

data class EditPatientAppointmentRequest(
    val DoctorId:Int?,
    val DoctorWorkingDayTimeId:Int?,
    val AppointmentDate:String?,
): Serializable
