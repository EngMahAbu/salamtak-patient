package com.korashiGroup.salamtakPatient.model.offers

import java.io.Serializable

data class OfferDoctor(
    val doctorId:Int,
    val clinkId:Int,
    val fess:String,
    val name:String,
    val image:String,
    val offerId:Int,
    val specialist:String,
):Serializable
