package com.korashiGroup.salamtakPatient.model.register

import java.io.Serializable

data class UserForgetInfo(
    var code: Int,
    val phone: String,
    val userId:Int,
): Serializable
