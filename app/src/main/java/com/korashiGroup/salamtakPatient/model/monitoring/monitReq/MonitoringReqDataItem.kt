package com.korashiGroup.salamtakPatient.model.monitoring.monitReq

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MonitoringReqDataItem(

	@field:SerializedName("StreetName")
	val streetName: String? = null,

	@field:SerializedName("FloorNumber")
	val floorNumber: String? = null,

	@field:SerializedName("MonitoringDeviceId")
	val monitoringDeviceId: Int? = null,

	@field:SerializedName("Hours")
	val hours: String? = null,

	@field:SerializedName("AreaId")
	val areaId: Int? = null,

	@field:SerializedName("Text")
	val text: String? = null,

	@field:SerializedName("CityName")
	val cityName: String? = null,

	@field:SerializedName("ApartmentNumber")
	val apartmentNumber: String? = null,

	@field:SerializedName("AreaName")
	val areaName: String? = null,

	@field:SerializedName("NumberOfDays")
	val numberOfDays: Int? = null,

//	@field:SerializedName("MonitoringDevice")
//	val monitoringDevice: MonitoringDevice? = null,

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("ToTime")
	val toTime: String? = null,

	@field:SerializedName("CityId")
	val cityId: Int? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("BuildingNumber")
	val buildingNumber: String? = null,

	@field:SerializedName("FromDate")
	val fromDate: String? = null,

	@field:SerializedName("ToDate")
	val toDate: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("FromTime")
	val fromTime: String? = null
) : Parcelable