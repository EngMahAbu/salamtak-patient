package com.korashiGroup.salamtakPatient.model.nursingServices.service

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ServiceLevelData(
	@SerializedName("Id")
	val id: Int? = null,

	@SerializedName("Name")
	val name: String? = null,

	@SerializedName("NameAr")
	val nameAr: String? = null,

	@SerializedName("DisplayName")
	val displayName: String? = null,

	@SerializedName("Inactive")
	val inactive: Boolean? = null
) : Parcelable