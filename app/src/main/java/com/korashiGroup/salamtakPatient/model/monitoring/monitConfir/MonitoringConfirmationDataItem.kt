package com.korashiGroup.salamtakPatient.model.monitoring.monitConfir

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MonitoringConfirmationDataItem(

	@field:SerializedName("UserName")
	val userName: String? = null,

	@field:SerializedName("MonitoringDeviceId")
	val monitoringDeviceId: Int? = null,

	@field:SerializedName("MonitoringDevicesLevel")
	val monitoringDevicesLevel: String? = null,

	@field:SerializedName("Text")
	val text: String? = null,

	@field:SerializedName("ToHour")
	val toHour: String? = null,

	@field:SerializedName("IsShow")
	val isShow: Boolean? = null,

	@field:SerializedName("FromHour")
	val fromHour: String? = null,

	@field:SerializedName("NumberOfDays")
	val numberOfDays: Int? = null,

	@field:SerializedName("MonitoringDeviceLevelId")
	val monitoringDeviceLevelId: String? = null,

	@field:SerializedName("ToDay")
	val toDay: String? = null,

	@field:SerializedName("TotalFees")
	val totalFees: Int? = null,

	@field:SerializedName("MonitoringDeviceRequestId")
	val monitoringDeviceRequestId: Int? = null,

	@field:SerializedName("FeesPerDay")
	val feesPerDay: Int? = null,

	@field:SerializedName("MonitoringDeviceName")
	val monitoringDeviceName: String? = null,

	@field:SerializedName("PayNowUrl")
	val payNowUrl: String? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("MonitoringDeviceLevelName")
	val monitoringDeviceLevelName: String? = null,

	@field:SerializedName("FromDay")
	val fromDay: String? = null,

	@field:SerializedName("IsPaid")
	val isPaid: Boolean? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("DoctorOrAgentName")
	val doctorOrAgentName: String? = null,

	@field:SerializedName("PaymentStatus")
	val paymentStatus: String? = null
) : Parcelable