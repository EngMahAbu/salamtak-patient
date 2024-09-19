package com.korashiGroup.salamtakPatient.model.invoice


import com.google.gson.annotations.SerializedName

data class DoctorInvoiceResponse(

    @SerializedName("Data")
    val `data`: List<PatientData?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class PatientData(
    @SerializedName("Date")
    val date: String?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Items")
    val items: List<Item?>?,
    @SerializedName("PaidAmount")
    val paidAmount: Int?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("TotalAmount")
    val totalAmount: Int?
)
data class Item(
    @SerializedName("Date")
    val date: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("InvoiceId")
    val invoiceId: Int?,
    @SerializedName("Paid")
    val paid: Double?,
    @SerializedName("Service")
    val service: String?,
    @SerializedName("ServiceFee")
    val serviceFee: Double?
)