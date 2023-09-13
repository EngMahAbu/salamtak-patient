package com.korashiGroup.salamtakPatient.model.booking


data class AppointmentBooking(
    val time:String?,
    val timeInterval:Int?,
    val DoctorWorkingDayTimeId:Int?,
    val fees:Int?,
    val MedicalExaminationTypeName:String?,
    val bookingAppointments:List<String>?

)
