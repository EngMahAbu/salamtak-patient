package com.korashiGroup.salamtakPatient.model.profile


import com.google.gson.annotations.SerializedName

data class GetPatientInfoResponse(
    @SerializedName("Data")
    val data: PatientDataInfo?,
    @SerializedName("Message")
    val message: String?
)

data class PatientDataInfo(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("ApartmentNo")
    val apartmentNo: String?,
    @SerializedName("AreaId")
    val areaId: Int?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("Birthdate")
    val birthdate: String?,
    @SerializedName("BlockNo")
    val blockNo: String?,
    @SerializedName("CityId")
    val cityId: Int?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("CountryId")
    val countryId: Int?,
    @SerializedName("CountryName")
    val countryName: String?,
    @SerializedName("EmergencyContact")
    val emergencyContact: String?,
    @SerializedName("FamilyName")
    val familyName: String?,
    @SerializedName("FamilyNameAr")
    val familyNameAr: String?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("FirstNameAr")
    val firstNameAr: String?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("FullNameAr")
    val fullNameAr: String?,
    @SerializedName("GenderId")
    val genderId: Int?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("Latitude")
    val latitude: Any?,
    @SerializedName("Longitude")
    val longitude: Any?,
    @SerializedName("MiddelName")
    val middelName: String?,
    @SerializedName("MiddelNameAr")
    val middelNameAr: String?,
    @SerializedName("NationalityId")
    val nationalityId: Int?,
    @SerializedName("NationalityName")
    val nationalityName: String?,
    @SerializedName("OccupationId")
    val occupationId: String?,
    @SerializedName("OccupationName")
    val occupationName: String?,
    @SerializedName("UserId")
    val userId: Int?
)