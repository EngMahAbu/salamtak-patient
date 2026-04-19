package com.korashiGroup.salamtakPatient.model.medicalConsultant

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MedicalConsultantDataItem(

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("UserId")
	val userId: Int? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("SpecialityId")
	val specialityId: Int? = null,

	@field:SerializedName("Question")
	val question: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("SpecialistName")
	val specialistName: String? = null,

	@field:SerializedName("Symptoms")
	val symptoms: String? = null
) : Parcelable