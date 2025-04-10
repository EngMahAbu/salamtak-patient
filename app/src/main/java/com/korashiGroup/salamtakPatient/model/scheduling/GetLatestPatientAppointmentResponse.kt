package com.korashiGroup.salamtakPatient.model.scheduling


import com.google.gson.annotations.SerializedName

data class GetLatestPatientAppointmentResponse(
    @SerializedName("Data")
    val `data`: LatestPatientAppointment?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)

data class LatestPatientAppointment(
    @SerializedName("ApartmentNo")
    val apartmentNo: String?,
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("AvgRate")
    val avgRate: Int?,
    @SerializedName("BlockNo")
    val blockNo: String?,
    @SerializedName("CanRate")
    val canRate: Boolean?,
    @SerializedName("ClinicAddress")
    val clinicAddress: String?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicLatitude")
    val clinicLatitude: String?,
    @SerializedName("ClinicLongitude")
    val clinicLongitude: String?,
    @SerializedName("ClinicName")
    val clinicName: String?,
    @SerializedName("ClinicPhone")
    val clinicPhone: String?,
    @SerializedName("DiscountPerc")
    val discountPerc: Any?,
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorImage")
    val doctorImage: String?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("DoctorWorkingDayTimeId")
    val doctorWorkingDayTimeId: Int?,
    @SerializedName("Fees")
    val fees: Int?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("HealthEntity_OffersID")
    val healthEntityOffersID: Int?,
    @SerializedName("IsCancel")
    val isCancel: Boolean?,
    @SerializedName("IsCompleted")
    val isCompleted: Boolean?,
    @SerializedName("IsSeen")
    val isSeen: Boolean?,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int?,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String?,
    @SerializedName("PatientId")
    val patientId: Int?,
    @SerializedName("PriceAfter")
    val priceAfter: Any?,
    @SerializedName("PriceBefore")
    val priceBefore: Any?,
    @SerializedName("rateCount")
    val rateCount: Int?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?
)