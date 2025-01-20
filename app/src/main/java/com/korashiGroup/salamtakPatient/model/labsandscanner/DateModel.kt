package com.korashiGroup.salamtakPatient.model.labsandscanner

import java.time.LocalDate

data class DateModel(
    var data: LocalDate,
    var dateInArabic:String,
    var startDate:String,
    var endDate:String
)
