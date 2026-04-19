package com.korashiGroup.salamtakPatient.model.mobileClinic

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MobileClinicResponse(

	@field:SerializedName("MessageCode")
	val messageCode: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: MobileClinicDataItem? = null,

	@field:SerializedName("Success")
	val success: Boolean? = null
) : Parcelable