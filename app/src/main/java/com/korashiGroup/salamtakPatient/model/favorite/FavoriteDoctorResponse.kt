package com.korashiGroup.salamtakPatient.model.favorite


import com.google.gson.annotations.SerializedName

data class FavoriteDoctorResponse(
    @SerializedName("Data")
    val data: List<FavoriteDoctor>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)

data class FavoriteDoctor(
    @SerializedName("Comment")
    val comment: String,
    @SerializedName("DoctorId")
    val doctorId: Int,
    @SerializedName("DoctorName")
    val doctorName: String,
    @SerializedName("getDoctor")
    val getDoctor: GetDoctor,
    @SerializedName("SpecialistName")
    val specialistName: String
)

data class GetDoctor(
    @SerializedName("Birthday")
    val birthday: String,
    @SerializedName("BirthdayStr")
    val birthdayStr: Any,
    @SerializedName("clinicDtos")
    val clinicDtos: Any,
    @SerializedName("Code")
    val code: String,
    @SerializedName("DoctorInfo")
    val doctorInfo: Any,
    @SerializedName("DoctorInfoAr")
    val doctorInfoAr: Any,
    @SerializedName("DoctorSubSpecialist")
    val doctorSubSpecialist: Any,
    @SerializedName("Email")
    val email: String,
    @SerializedName("FacebookAccount")
    val facebookAccount: Any,
    @SerializedName("FirstName")
    val firstName: Any,
    @SerializedName("FirstNameAr")
    val firstNameAr: Any,
    @SerializedName("GenderId")
    val genderId: Int,
    @SerializedName("GenderName")
    val genderName: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Image")
    val image: String,
    @SerializedName("IsFavoiritDoctor")
    val isFavoiritDoctor: Boolean,
    @SerializedName("LastName")
    val lastName: Any,
    @SerializedName("LastNameAr")
    val lastNameAr: Any,
    @SerializedName("LicenseNumber")
    val licenseNumber: String,
    @SerializedName("MiddelName")
    val middelName: Any,
    @SerializedName("MiddelNameAr")
    val middelNameAr: Any,
    @SerializedName("NationalId")
    val nationalId: String,
    @SerializedName("NationalityId")
    val nationalityId: Int,
    @SerializedName("NationalityName")
    val nationalityName: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("ProfileStatus")
    val profileStatus: Int,
    @SerializedName("SeniorityLevelId")
    val seniorityLevelId: Int,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String,
    @SerializedName("SpecialistId")
    val specialistId: Int,
    @SerializedName("SpecialistName")
    val specialistName: String,
    @SerializedName("StatusId")
    val statusId: Int,
    @SerializedName("StatusName")
    val statusName: String,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String>,
    @SerializedName("SyndicateId")
    val syndicateId: String,
    @SerializedName("UserId")
    val userId: Int,
    @SerializedName("Website")
    val website: Any
)