package com.korashiGroup.salamtakPatient.model.askandanswer

data class QuestionRequest(
    val SpecialistID:Int,
    val Question:String,
    val QDetails:String,
    val IsForMe:Boolean,
    val GenderID:Int,
    val Age:Int
)
