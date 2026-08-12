package com.korashiGroup.salamtakPatient.model.monitoring.monitReq

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceDataItem(

	@field:SerializedName("Price")
	val price: String? = null,

	@field:SerializedName("DisplayName")
	val displayName: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("Image")
	val image: String? = null
) : Parcelable