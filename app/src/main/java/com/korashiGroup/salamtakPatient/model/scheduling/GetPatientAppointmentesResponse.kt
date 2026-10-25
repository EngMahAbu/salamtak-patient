package com.korashiGroup.salamtakPatient.model.scheduling


import com.google.gson.annotations.SerializedName

data class GetPatientAppointmentesResponse(
    @SerializedName("Data")
    val data: List<DataAppointment?>?,
    @SerializedName("Message")
    val message: String?
)

data class DataAppointment(
    @SerializedName("AppointmentDate")
    val appointmentDate: String?,
    @SerializedName("AppointmentId")
    val appointmentId: Int?,
    @SerializedName("CityName", alternate = ["cityName", "City", "city", "cityNameAr", "CityNameAr", "city_name", "City_Name", "clinicCityName", "ClinicCityName", "clinicCity", "ClinicCity", "CityNameEn", "cityNameEn"])
    val cityName: String?,
    @SerializedName("AreaName", alternate = ["areaName", "Area", "area", "areaNameAr", "AreaNameAr", "area_name", "Area_Name", "clinicAreaName", "ClinicAreaName", "clinicArea", "ClinicArea", "AreaNameEn", "areaNameEn", "subAreaName", "SubAreaName"])
    val areaName: String?,
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
    @SerializedName("DoctorId")
    val doctorId: Int?,
    @SerializedName("DoctorImage")
    val doctorImage: String?,
    @SerializedName("DoctorName")
    val doctorName: String?,
    @SerializedName("Fees")
    val fees: Int?,
    @SerializedName("IsCancel")
    val isCancel: Boolean?,
    @SerializedName("IsSeen")
    val isSeen: Boolean?,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Int?,
    @SerializedName("MedicalExaminationTypeName")
    val medicalExaminationTypeName: String?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    @SerializedName("IsCompleted")
    val IsCompleted: Boolean?,
    @SerializedName("BlockNo")
    val BlockNo: String?,
    @SerializedName("FloorNo")
    val FloorNo: String?,
    @SerializedName("ApartmentNo")
    val ApartmentNo: String?,
    @SerializedName("HealthEntity_OffersID")
    val HealthEntity_OffersID: Int?,
    val ClinicPhone: String?,


)