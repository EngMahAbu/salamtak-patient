package com.korashiGroup.salamtakPatient.model.labsandscanner


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class LabsBranchesResponse(
    @SerializedName("Data")
    val `data`: List<DataBranches?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
): Parcelable
@Parcelize
data class DataBranches(
    @SerializedName("AreaName")
    val areaName: String?,
    @SerializedName("AvailableServices")
    val availableServices: List<AvailableService?>?,
    @SerializedName("BranchAddress")
    val branchAddress: String?,
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("CityName")
    val cityName: String?,
    @SerializedName("LabId")
    val labId: Int?,
    @SerializedName("LabImage")
    val labImage: String?,
    @SerializedName("LabName")
    val labName: String?,
    @SerializedName("ServiceAvailabilityMessage")
    val serviceAvailabilityMessage: String?
):Parcelable
@Parcelize
data class AvailableService(
    @SerializedName("ServiceId")
    val serviceId: Int?,
    @SerializedName("ServiceName")
    val serviceName: String?,
    @SerializedName("ServicePrice")
    val servicePrice: Double?
):Parcelable