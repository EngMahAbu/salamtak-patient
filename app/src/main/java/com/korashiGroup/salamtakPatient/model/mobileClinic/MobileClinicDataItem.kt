package com.korashiGroup.salamtakPatient.model.mobileClinic

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MobileClinicDataItem(

	@field:SerializedName("Floor")
	val floor: String? = null,

	@field:SerializedName("SubSpecialistName")
	val subSpecialistName: String? = null,

	@field:SerializedName("SepcialRecuirements")
	val sepcialRecuirements: String? = null,

	@field:SerializedName("AreaId")
	val areaId: String? = null,

	@field:SerializedName("SpecialityId")
	val specialityId: String? = null,

	@field:SerializedName("SubSpecialistyId")
	val subSpecialistyId: String? = null,

	@field:SerializedName("CityName")
	val cityName: String? = null,

	@field:SerializedName("AreaName")
	val areaName: String? = null,

	@field:SerializedName("MobailPhone")
	val mobailPhone: String? = null,

	@field:SerializedName("GenderId")
	val genderId: String? = null,

	@field:SerializedName("CityId")
	val cityId: String? = null,

	@field:SerializedName("SeniorityLevelId")
	val seniorityLevelId: String? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("BuildingNumber")
	val buildingNumber: String? = null,

	@field:SerializedName("Describtion")
	val describtion: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("Street")
	val street: String? = null,

	@field:SerializedName("SeniorityLevelName")
	val seniorityLevelName: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("Aparment")
	val aparment: String? = null,

	@field:SerializedName("SpecialistName")
	val specialistName: String? = null,

	@field:SerializedName("Symptoms")
	val symptoms: String? = null,

	@field:SerializedName("GenderName")
	val genderName: String? = null
) : Parcelable