package com.korashiGroup.salamtakPatient.model.register

import java.io.Serializable

data class UserForgetInfo(
    val code: Int,
    val phone: String,
    val userId:Int,
): Serializable
