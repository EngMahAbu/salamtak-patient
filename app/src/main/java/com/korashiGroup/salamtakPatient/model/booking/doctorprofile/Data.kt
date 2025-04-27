package com.korashiGroup.salamtakPatient.model.booking.doctorprofile


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("Birthday")
    val birthday: String?,
    @SerializedName("BirthdayStr")
    val birthdayStr: String?,
    @SerializedName("clinicDtos")
    val clinicDtos: List<ClinicDto?>?,
    @SerializedName("Code")
    val code: String?,
    @SerializedName("DoctorInfo")
    val doctorInfo: Any?,
    @SerializedName("DoctorInfoAr")
    val doctorInfoAr: Any?,
    @SerializedName("DoctorSubSpecialist")
    val doctorSubSpecialist: List<Int?>?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FacebookAccount")
    val facebookAccount: Any?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("FirstNameAr")
    val firstNameAr: String?,
    @SerializedName("GenderId")
    val genderId: Int?,
    @SerializedName("GenderName")
    val genderName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("IsFavoiritDoctor")
    val isFavoiritDoctor: Boolean?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("LastNameAr")
    val lastNameAr: String?,
    @SerializedName("LicenseNumber")
    val licenseNumber: String?,
    @SerializedName("MedicalExamationTypes")
    val medicalExamationTypes: List<MedicalExamationType?>?,
    @SerializedName("MiddelName")
    val middelName: String?,
    @SerializedName("MiddelNameAr")
    val middelNameAr: String?,
    @SerializedName("NationalId")
    val nationalId: String?,
    @SerializedName("NationalityId")
    val nationalityId: Int?,
    @SerializedName("NationalityName")
    val nationalityName: String?,
    @SerializedName("NickName")
    val nickName: Any?,
    @SerializedName("NickNameAr")
    val nickNameAr: Any?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("ProfileStatus")
    val profileStatus: Int?,
    @SerializedName("SeniorityLevelId")
    val seniorityLevelId: Int?,
    @SerializedName("SeniorityLevelName")
    val seniorityLevelName: String?,
    @SerializedName("SpecialistId")
    val specialistId: Int?,
    @SerializedName("SpecialistName")
    val specialistName: String?,
    @SerializedName("StatusId")
    val statusId: Int?,
    @SerializedName("StatusName")
    val statusName: String?,
    @SerializedName("Sub_SpecialistList")
    val subSpecialistList: List<SubSpecialist?>?,
    @SerializedName("SubSpecialistName")
    val subSpecialistName: List<String?>?,
    @SerializedName("SyndicateId")
    val syndicateId: String?,
    @SerializedName("UserId")
    val userId: Int?,
    @SerializedName("Website")
    val website: Any?
)