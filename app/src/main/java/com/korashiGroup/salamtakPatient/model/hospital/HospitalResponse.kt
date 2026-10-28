package com.korashiGroup.salamtakPatient.model.hospital

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HospitalResponse(
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Data")
    val data: List<HospitalItem>?,
    @SerializedName("Success")
    val success: Boolean?
) : Serializable

data class HospitalItem(
    @SerializedName("HospitalInfoId")
    val hospitalInfoId: Int?,
    @SerializedName("HospitalId")
    val hospitalId: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("NameAr")
    val nameAr: String?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Address")
    val address: String?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("Latitude")
    val latitude: String?,
    @SerializedName("Longitude")
    val longitude: String?,
    @SerializedName("Inactive")
    val inactive: Boolean?,
    @SerializedName("StatusId")
    val statusId: Int?,
    @SerializedName("Bio")
    val bio: String?,
    @SerializedName("Phones")
    val phones: List<String>?,
    @SerializedName("Services")
    val services: List<String>?,
    @SerializedName("Insurance")
    val insurance: List<String>?,
    @SerializedName("Features")
    val features: List<String>?,
    @SerializedName("Branches")
    val branches: List<HospitalBranch>?,
    @SerializedName("Doctors")
    val doctors: List<Any>?
) : Serializable

data class HospitalBranch(
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("HospitalId")
    val hospitalId: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("NameAr")
    val nameAr: String?,
    @SerializedName("CityId")
    val cityId: Int?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("AreaId")
    val areaId: Int?,
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("Address")
    val address: String?,
    @SerializedName("Latitude")
    val latitude: String?,
    @SerializedName("Longitude")
    val longitude: String?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("IsMain")
    val isMain: Boolean?,
    @SerializedName("IsActive")
    val isActive: Boolean?
) : Serializable
