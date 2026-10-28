package com.korashiGroup.salamtakPatient.model

data class ImageServices(
    val image_onboarding: Int,
    val textService: String,
    val textServiceArabic: String? = "",
    val color: Int = 0,
    val Id: Int? = 0,
    val image: String? = null,
    val isSpacer: Boolean = false
)
