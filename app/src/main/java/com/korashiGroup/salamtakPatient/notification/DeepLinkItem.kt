package com.korashiGroup.salamtakPatient.notification

import java.io.Serializable

data class DeepLinkItem(
    val id:String?,
    val token:String?,
    val DoctorName:String?,
    val DoctorImage:String?,
    val Channelname:String?,
    val date:String?,
): Serializable
