package com.korashiGroup.salamtakPatient.model.docotorsearch.doc


import com.google.gson.annotations.SerializedName

data class HealthEntity(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("ApartmentNo")
    val apartmentNo: Any?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("BlockNo")
    val blockNo: Any?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicLatitude")
    val clinicLatitude: String?,
    @SerializedName("ClinicLongitude")
    val clinicLongitude: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FixedFee")
    val fixedFee: Double?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Phone")
    val phone: String?
)