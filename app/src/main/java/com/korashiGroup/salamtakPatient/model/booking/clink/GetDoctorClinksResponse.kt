package com.korashiGroup.salamtakPatient.model.booking.clink


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetDoctorClinksResponse(
    @SerializedName("Data")
    val data: Data?,
    @SerializedName("Message")
    val message: String?
): Serializable
data class Data(
    @SerializedName("Birthday")
    val birthday: String?,
    @SerializedName("BirthdayStr")
    val birthdayStr: String?,
    @SerializedName("clinicDtos")
    val clinicDtos: List<ClinicDto?>?,
    @SerializedName("CurrentClinicDtos")
    val currentClinicDtos: CurrentClinicDtos?,
    @SerializedName("Code")
    val code: String?,
    @SerializedName("DoctorInfo")
    val doctorInfo: String?,
    @SerializedName("DoctorInfoAr")
    val doctorInfoAr: Any?,
    @SerializedName("DoctorSubSpecialist")
    val doctorSubSpecialist: List<Int?>?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FacebookAccount")
    val facebookAccount: Any?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("FirstNameAr")
    val firstNameAr: String?,
    @SerializedName("GenderId")
    val genderId: Int?,
    @SerializedName("GenderName")
    val genderName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("LastNameAr")
    val lastNameAr: String?,
    @SerializedName("LicenseNumber")
    val licenseNumber: String?,
    @SerializedName("MiddelName")
    val middelName: String?,
    @SerializedName("MiddelNameAr")
    val middelNameAr: String?,
    @SerializedName("NationalId")
    val nationalId: String?,
    @SerializedName("NationalityId")
    val nationalityId: Int?,
    @SerializedName("NationalityName")
    val nationalityName: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?,
    @SerializedName("SeniorityLevelId")
    val seniorityLevelId: Int?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistId")
    val specialistId: Int?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("StatusId")
    val statusId: Int?,
    @SerializedName("StatusName")
    val statusName: String?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    @SerializedName("SyndicateId")
    val syndicateId: String?,
    @SerializedName("UserId")
    val userId: Int?,
    @SerializedName("Website")
    val website: Any?,
    @SerializedName("IsFavoiritDoctor")
    val isFavoiritDoctor: Boolean?,

    ): Serializable
data class ClinicDto(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("ApartmentNo")
    val apartmentNo: Any?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("BlockNo")
    val blockNo: String?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicLatitude")
    val clinicLatitude: String?,
    @SerializedName("ClinicLongitude")
    val clinicLongitude: Any?,
    @SerializedName("Email")
    val email: Any?,
    @SerializedName("FixedFee")
    val fixedFee: Int?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Services")
    val services: List<String>
): Serializable
data class CurrentClinicDtos(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Features")
    val features: List<Any>?,
    @SerializedName("FixedFee")
    val fixedFee: Int?,
    @SerializedName("Gallary")
    val gallary: List<Any>?,
    @SerializedName("Insurance")
    val insurance: List<Any>?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Services")
    val services: List<Any>?,
    @SerializedName("SubAreaName")
    val subAreaName: String?
): Serializable