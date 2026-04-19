package com.korashiGroup.salamtakPatient.model.nursingServices.service

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ServiceLevelResponse(

	@SerializedName("Message")
	val message: String? = null,

	@SerializedName("MessageCode")
	val messageCode: Int? = null,

	@SerializedName("Data")
	val data: List<ServiceLevelData?>? = null,

	@SerializedName("Success")
	val success: Boolean? = null
) : Parcelable