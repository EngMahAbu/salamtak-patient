package com.korashiGroup.salamtakPatient.model.booking

import java.io.Serializable

data class PatientAppointmentRequest(
    val DoctorId:Int?,
    val DoctorWorkingDayTimeId:Int?,
    val AppointmentDate:String?,
    val IsBook:Boolean?,
    val image:String?,
    var PaymentMethodID:Int=0,
    var Fawryrefnum:String="",
    var FawrySign:String="",
    var Patient_InsuranceID:Int?=null,
    var HealthEntity_OffersID:Int?=null,
    var IsBookingForAnotherPatient:Boolean?=null,
    var AnotherPatientFullName:String?=null,
    var AnotherPatientPhone:String?=null,
): Serializable
