package com.korashiGroup.salamtakPatient.model.docotorsearch.doc


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("Items")
    val items: List<Item?>?,
    @SerializedName("TotalCount")
    val totalCount: Int?
)