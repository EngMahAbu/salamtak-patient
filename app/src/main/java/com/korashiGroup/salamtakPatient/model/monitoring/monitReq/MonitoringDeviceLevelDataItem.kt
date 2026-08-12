package com.korashiGroup.salamtakPatient.model.monitoring.monitReq

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MonitoringDeviceLevelDataItem(

	@field:SerializedName("DisplayName")
	val displayName: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null
) : Parcelable