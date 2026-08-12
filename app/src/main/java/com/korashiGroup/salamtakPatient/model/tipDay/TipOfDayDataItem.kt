package com.korashiGroup.salamtakPatient.model.tipDay

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class TipOfDayDataItem(

	@field:SerializedName("DisplayName")
	val displayName: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("Image")
	val image: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("NameAr")
	val nameAr: String? = null
) : Parcelable