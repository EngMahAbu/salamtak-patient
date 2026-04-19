package com.korashiGroup.salamtakPatient.model.nursingServices

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NursingResponse(
	@SerializedName("Message")
	val message: String? = null,

	@SerializedName("MessageCode")
	val messageCode: Int? = null,

	@SerializedName("Success")
	val success: Boolean? = null,

	@SerializedName("Data")
	val data: NursingServiceDataItem? = null

) : Parcelable