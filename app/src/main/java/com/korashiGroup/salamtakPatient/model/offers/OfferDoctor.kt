package com.korashiGroup.salamtakPatient.model.offers

import java.io.Serializable

data class OfferDoctor(
    val doctorId:Int,
    val clinkId:Int,
    val fess: Int?,
    val name:String,
    val image:String,
    val offerId:String,
    val specialist:String,
):Serializable
