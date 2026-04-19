package com.korashiGroup.salamtakPatient.model.nursingServices

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NursingServiceDataItem(
	@SerializedName("Id")
	val id: Int? = null,

	@SerializedName("FullName")
	val fullName: String? = null,

	@SerializedName("MobailPhone")
	val mobailPhone: String? = null,

	@SerializedName("BuildingNumber")
	val buildingNumber: String? = null,

	@SerializedName("Street")
	val street: String? = null,

	@SerializedName("Floor")
	val floor: String? = null,

	@SerializedName("Apartment")
	val apartment: String? = null,

	@SerializedName("CityId")
	val cityId: Int? = null,

	@SerializedName("CityName")
	val cityName: String? = null,

	@SerializedName("AreaId")
	val areaId: Int? = null,

	@SerializedName("AreaName")
	val areaName: String? = null,

	@SerializedName("FromDay")
	val fromDay: String? = null,

	@SerializedName("ToDay")
	val toDay: String? = null,

	@SerializedName("FromHour")
	val fromHour: String? = null,

	@SerializedName("ToHour")
	val toHour: String? = null,

	@SerializedName("GenderId")
	val genderId: Int? = null,

	@SerializedName("GenderName")
	val genderName: String? = null,

	@SerializedName("ServiceId")
	val serviceId: Int? = null, // 🔥 أهم تعديل

	@SerializedName("ServiceName")
	val serviceName: String? = null,

	@SerializedName("Service")
	val service: String? = null,

	@SerializedName("SpecialRequiremets")
	val specialRequiremets: String? = null,

	@SerializedName("CreatedDate")
	val createdDate: String? = null
) : Parcelable