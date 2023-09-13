package com.korashiGroup.salamtakPatient.model.askandanswer


import com.google.gson.annotations.SerializedName

data class QuestionAndAnswerResponse(
    @SerializedName("Data")
    val `data`: Answer,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class Answer(
    @SerializedName("Age")
    val age: Int,
    @SerializedName("Answer")
    val answer: Any,
    @SerializedName("DoctorID")
    val doctorID: Any,
    @SerializedName("GenderID")
    val genderID: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("IsForMe")
    val isForMe: Boolean,
    @SerializedName("PatientID")
    val patientID: Int,
    @SerializedName("QDetails")
    val qDetails: String,
    @SerializedName("Question")
    val question: String,
    @SerializedName("SpecialistID")
    val specialistID: Int,
    @SerializedName("SpecialityName")
    val specialityName: Any
)