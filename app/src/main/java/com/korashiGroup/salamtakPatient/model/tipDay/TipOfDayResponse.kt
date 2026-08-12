package com.korashiGroup.salamtakPatient.model.tipDay

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class TipOfDayResponse(

	@field:SerializedName("MessageCode")
	val messageCode: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: TipOfDayDataItem? = null,

	@field:SerializedName("Success")
	val success: Boolean? = null
) : Parcelable