package com.korashiGroup.salamtakPatient.model.rating

import java.io.Serializable

data class DotorDetails(
    val firstNameEn:String,
    val firstNameAr:String,
    val midNameEn:String,
    val midNameAr:String,
    val lastNameEn:String,
    val lastNameAr:String,
    val doctorId:Int,
    val image:String,
    val docotrSpec:String,

): Serializable