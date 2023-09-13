package com.korashiGroup.salamtakPatient.model.booking

data class BookingRequest(

    val ClinicId:Int?,
    val DayId:Int?,
    val MedicalExaminationTypeId:Int?,
    val BookDate:String?,

)
