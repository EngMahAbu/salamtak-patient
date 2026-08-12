package com.korashiGroup.salamtakPatient.model.monitoring.monitConfir

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MonitoringConfirmationResponse(

    @field:SerializedName("MessageCode")
	val messageCode: Int? = null,

    @field:SerializedName("Message")
	val message: String? = null,

    @field:SerializedName("Data")
	val data: List<MonitoringConfirmationDataItem?>? = null,

    @field:SerializedName("Success")
	val success: Boolean? = null
) : Parcelable