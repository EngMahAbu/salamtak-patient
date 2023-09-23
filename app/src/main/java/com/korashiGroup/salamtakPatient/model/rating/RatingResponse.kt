package com.korashiGroup.salamtakPatient.model.rating

data class RatingResponse(
    val Data: Rating,
    val Message: String,
    val MessageCode: Int,
    val Success: Boolean
)

data class Rating(
    val Statues: Boolean
)