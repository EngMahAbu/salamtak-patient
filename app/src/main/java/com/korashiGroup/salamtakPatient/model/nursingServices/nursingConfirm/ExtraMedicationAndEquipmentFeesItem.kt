package com.korashiGroup.salamtakPatient.model.nursingServices.nursingConfirm

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ExtraMedicationAndEquipmentFeesItem(

	@field:SerializedName("Amount")
	val amount: Int? = null,

	@field:SerializedName("Name")
	val name: String? = null
) : Parcelable