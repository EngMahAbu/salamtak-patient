package com.korashiGroup.salamtakPatient.model.booking

import java.io.Serializable

data class ClinicSchedualByClinicDayId(

    val clinicId:Int,
    val dayId:Int,
    val medicalExaminationId:Int,
    val formattedDate:String,
    val clinicName:String
): Serializable
