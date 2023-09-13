package com.korashiGroup.salamtakPatient.model.booking

import java.io.Serializable


data class AppointmentDetailBooking(
    val doctorName:String?,
    val doctorId:Int,
    val formattedDate:String?,
    val timeInterval:Int?,
    val DoctorWorkingDayTimeId:Int?,
    val fees:Int?,
    val MedicalExaminationTypeName:String?,
    val SpecialistName:String?,
    val image:String?

): Serializable
