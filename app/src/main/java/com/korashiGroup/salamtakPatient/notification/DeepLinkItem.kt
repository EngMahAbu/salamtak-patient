package com.korashiGroup.salamtakPatient.notification

import java.io.Serializable

data class DeepLinkItem(
    val token:String?,
    val DoctorName:String?,
    val DoctorImage:String?,
    val Channelname:String?,
    val date:String?,
    val link:String?
): Serializable
