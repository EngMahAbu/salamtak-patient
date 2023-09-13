package com.korashiGroup.salamtakPatient.model.booking

import java.io.Serializable

data class PatientAppointmentRequest(
    val DoctorId:Int?,
    val DoctorWorkingDayTimeId:Int?,
    val AppointmentDate:String?,
    val IsBook:Boolean?,
    val image:String?
): Serializable
