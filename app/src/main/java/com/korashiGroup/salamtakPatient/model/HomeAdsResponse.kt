package com.korashiGroup.salamtakPatient.model

data class HomeAdsResponse(
    val Data: List<HomeAdsData>,
)

data class HomeAdsData(
    val IsVideo: Boolean,
    val VideoLink: String
)