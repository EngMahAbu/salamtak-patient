package com.korashiGroup.salamtakPatient.model.booking.doctorprofile


import com.google.gson.annotations.SerializedName

data class ClinicDto(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("ApartmentNo")
    val apartmentNo: Any?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("Area_PackageID")
    val areaPackageID: Any?,
    @SerializedName("BlockNo")
    val blockNo: String?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("ClinicBio")
    val clinicBio: Any?,
    @SerializedName("ClinicId")
    val clinicId: Int?,
    @SerializedName("ClinicLatitude")
    val clinicLatitude: String?,
    @SerializedName("ClinicLongitude")
    val clinicLongitude: Any?,
    @SerializedName("Email")
    val email: Any?,
    @SerializedName("Features")
    val features: Any?,
    @SerializedName("FixedFee")
    val fixedFee: Int?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("FollowUp_Period")
    val followUpPeriod: Any?,
    @SerializedName("Gallary")
    val gallary: List<Any?>?,
    @SerializedName("Insurance")
    val insurance: Any?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Services")
    val services: List<Any?>?,
    @SerializedName("SubAreaName")
    val subAreaName: String?,
    @SerializedName("Videos")
    val videos: List<Any?>?
)