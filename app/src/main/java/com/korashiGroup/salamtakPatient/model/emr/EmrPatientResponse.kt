package com.korashiGroup.salamtakPatient.model.emr


import com.google.gson.annotations.SerializedName

data class EmrPatientResponse(
    @SerializedName("Data")
    val data: DataEmr,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class DataEmr(
    @SerializedName("AppointmentId")
    val appointmentId: Int,
    @SerializedName("Instructions")
    val instructions: String,
    @SerializedName("PatientEmrdetails")
    val patientEmrdetails: List<PatientEmrdetail>,
    @SerializedName("PatientEmrdocuments")
    val patientEmrdocuments: List<PatientEmrdocument>
)

data class PatientEmrdetail(
    @SerializedName("Description")
    val description: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Title")
    val title: String
)

data class PatientEmrdocument(
    @SerializedName("DocumentUrl")
    val documentUrl: String,
    @SerializedName("file")
    val `file`: Any,
    @SerializedName("Id")
    val id: Int
)