package com.korashiGroup.salamtakPatient.fragment.labsandscanner.model

data class LabsInfo(
    val typeId:Int,
    val labId:Int,
    val LabName:String,
    val serveciesId:MutableList<Int>,
    val status:Boolean
)
