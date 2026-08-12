package com.korashiGroup.salamtakPatient.model.nursingServices.nursingConfirm

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NursingConfirmResponse(

    @field:SerializedName("MessageCode")
	val messageCode: Int? = null,

    @field:SerializedName("Message")
	val message: String? = null,

    @field:SerializedName("Data")
	val data: List<NursingConfirmDataItem?>? = null,

    @field:SerializedName("Success")
	val success: Boolean? = null
) : Parcelable