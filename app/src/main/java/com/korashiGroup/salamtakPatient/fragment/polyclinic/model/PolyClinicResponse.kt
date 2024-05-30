package com.korashiGroup.salamtakPatient.fragment.polyclinic.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PolyClinicResponse(
    @SerializedName("Data")
    val `data`: List<PolyClinicsItem>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
): Serializable

data class PolyClinicsItem(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("ApartmentNo")
    val apartmentNo: String?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("Area_PackageID")
    val areaPackageID: Any?,
    @SerializedName("BlockNo")
    val blockNo: String?,
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
    @SerializedName("Features")
    val features: Any?,
    @SerializedName("FixedFee")
    val fixedFee: Int?,
    @SerializedName("FloorNo")
    val floorNo: Int?,
    @SerializedName("Gallary")
    val gallary: List<String>?,
    @SerializedName("Videos")
    val videos: List<String>?,
    @SerializedName("Insurance")
    val insurance: Any?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Services")
    val services: List<Any>?,
    @SerializedName("SubAreaName")
    val subAreaName: String?,
    @SerializedName("ClinicBio")
    val ClinicBio: String?
):Serializable