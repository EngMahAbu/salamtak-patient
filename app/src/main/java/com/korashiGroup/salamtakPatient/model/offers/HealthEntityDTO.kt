package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class HealthEntityDTO(
    @SerializedName("Address")
    val address: String,
    @SerializedName("ApartmentNo")
    val apartmentNo: String,
    @SerializedName("AreaId")
    val areaId: Int,
    @SerializedName("AreaName")
    val areaName: String,
    @SerializedName("Area_PackageID")
    val areaPackageID: Int,
    @SerializedName("BlockNo")
    val blockNo: String,
    @SerializedName("CityId")
    val cityId: Int,
    @SerializedName("CityName")
    val cityName: String,
    @SerializedName("ClinicId")
    val clinicId: Int,
    @SerializedName("CountryId")
    val countryId: Int,
    @SerializedName("Email")
    val email: String,
    @SerializedName("FixedFee")
    val fixedFee: Any,
    @SerializedName("FloorNo")
    val floorNo: Int,
    @SerializedName("HealthEntityInsID")
    val healthEntityInsID: Any,
    @SerializedName("HealthEntityInsName")
    val healthEntityInsName: Any,
    @SerializedName("HealthEntityPhoneDtos")
    val healthEntityPhoneDtos: List<String>,
    @SerializedName("HealthEntityServiceName")
    val healthEntityServiceName: List<String>,
    @SerializedName("HealthEntityServices")
    val healthEntityServices: List<Int>,
    @SerializedName("Inactive")
    val inactive: Boolean,
    @SerializedName("Latitude")
    val latitude: String,
    @SerializedName("Logo")
    val logo: String,
    @SerializedName("Longitude")
    val longitude: String,
    @SerializedName("MedicalExaminationTypeId")
    val medicalExaminationTypeId: Any,
    @SerializedName("Name")
    val name: String,
    @SerializedName("NameAr")
    val nameAr: String,
    @SerializedName("Street")
    val street: Any,
    @SerializedName("SubAreaID")
    val subAreaID: Int,
    @SerializedName("SubAreaName")
    val subAreaName: String
)