package com.korashiGroup.salamtakPatient.model.nursingServices.nursingConfirm

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NursingConfirmDataItem(

	@field:SerializedName("ServiceLevel")
	val serviceLevel: String? = null,

	@field:SerializedName("ExtraMedicationAndEquipmentFees")
	val extraMedicationAndEquipmentFees: List<ExtraMedicationAndEquipmentFeesItem?>? = null,

	@field:SerializedName("UserName")
	val userName: String? = null,

	@field:SerializedName("Text")
	val text: String? = null,

	@field:SerializedName("ToHour")
	val toHour: String? = null,

	@field:SerializedName("IsShow")
	val isShow: Boolean? = null,

	@field:SerializedName("FromHour")
	val fromHour: String? = null,

	@field:SerializedName("NursingServiceId")
	val nursingServiceId: Int? = null,

	@field:SerializedName("NumberOfDays")
	val numberOfDays: Int? = null,

	@field:SerializedName("GenderId")
	val genderId: Int? = null,

	@field:SerializedName("ToDay")
	val toDay: String? = null,

	@field:SerializedName("TotalFees")
	val totalFees: String? = null,

	@field:SerializedName("FeesPerDay")
	val feesPerDay: String? = null,

	@field:SerializedName("PayNowUrl")
	val payNowUrl: String? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("FromDay")
	val fromDay: String? = null,

	@field:SerializedName("IsPaid")
	val isPaid: Boolean? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("PaymentStatus")
	val paymentStatus: String? = null,

	@field:SerializedName("GenderName")
	val genderName: String? = null
) : Parcelable