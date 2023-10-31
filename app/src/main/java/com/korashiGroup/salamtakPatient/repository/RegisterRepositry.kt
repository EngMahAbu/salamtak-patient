package com.korashiGroup.salamtakPatient.repository

import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.model.askandanswer.QuestionRequest
import com.korashiGroup.salamtakPatient.model.booking.EditPatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.profile.LocationRequest
import com.korashiGroup.salamtakPatient.model.register.*
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.SpUtil
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PartMap
import retrofit2.http.Query
import javax.inject.Inject

class RegisterRepositry @Inject constructor(private val apiService: ApiService) {
    @Inject
    lateinit var sp: SpUtil
    suspend fun login(culture: String, loginUser: LoginUser) = apiService.Login(culture, loginUser)
    suspend fun registerUser(registerUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.registerUser(it, registerUser) }

    suspend fun SendOTP(registerUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.SendOTP(it, registerUser) }

    suspend fun createUser(createUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.createUser(it, createUser) }

    suspend fun resetPassword(resetRequest: ResetRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.resetPassword(it, resetRequest) }

    suspend fun updatePassword(resetChangePassword: ResetChangePassword) =
        sp.getUserLang(Constans.Language)?.let { apiService.updatePassword(it, resetChangePassword) }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.changePassword(it, changePasswordRequest) }

    suspend fun getHomeAds() =
        sp.getUserLang(Constans.Language)?.let { apiService.getHomeAds(it, 1, true) }

    suspend fun getCountries() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getCountries(it) }

    suspend fun getSpecialist() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getSpecialist(it) }

    suspend fun getMedicalExamination() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getMedicalExamination(it) }

    suspend fun getSubSpecialist(specialListId: Int) =
        sp.getUserLang(Constans.Language)?.let { apiService.getSubSpecialist(it, specialListId) }

    suspend fun getBloodTypes() =
        sp.getUserLang(Constans.Language)?.let { apiService.getBloodTypes(it) }

    suspend fun getMedicineAllergy() =
        sp.getUserLang(Constans.Language)?.let { apiService.getMedicineAllergy(it) }

    suspend fun getFoodAllergy() =
        sp.getUserLang(Constans.Language)?.let { apiService.getFoodAllergy(it) }

    suspend fun getSeniorityLevel() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getSeniorityLevel(it) }

    suspend fun getAllCities() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllCities(it) }

    suspend fun getAreasByCityId(cityId: Int) =
        sp.getUserLang(Constans.Language)?.let { apiService.getAreasByCityId(it, cityId) }

    suspend fun searchDoctors(doctorSearchRequest: DoctorSearchRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.searchDoctors(it, doctorSearchRequest) }

    suspend fun getClinicSchedualByClinicDayId(
        ClinicId: Int, DayId: Int,
        MedicalExaminationTypeId: Int,
        BookDate: String
    ) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getClinicSchedualByClinicDayId(it, ClinicId, DayId, MedicalExaminationTypeId, BookDate) }

    suspend fun createPatientAppointment(appointmentRequest: PatientAppointmentRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createPatientAppointment(it, appointmentRequest) }

    suspend fun getDoctorProfileByDoctorId(DoctorId: Int) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getDoctorProfileByDoctorId(it, DoctorId) }

    suspend fun getPatientAppointmentes(medicalExaminationTypeId: Int?) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getPatientAppointmentes(it, medicalExaminationTypeId) }

    suspend fun cancelPatientAppointment(AppointmentId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.cancelPatientAppointment(it, AppointmentId) }

    suspend fun createPatientProfile(partmap: MultipartBody) =
        sp.getUserLang(Constans.Language)?.let { apiService.createPatientProfile(it, partmap) }

    suspend fun updatePatientProfile(partmap: MultipartBody) =
        sp.getUserLang(Constans.Language)?.let { apiService.updatePatientProfile(it, partmap) }

    suspend fun sendPatientLocation(locationRequest: LocationRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.sendPatientLocation(it, locationRequest) }

    suspend fun CreatePatientMedicallInfo(medicalInfoRequest: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.CreatePatientMedicallInfo(it, medicalInfoRequest) }

    suspend fun getHealthEntityPagedList(
        CityId: Int, AreaId: Int, HealthEntityTypeId: Int,
        MaxResultCount: Int, SkipCount: Int
    ) =
        sp.getUserLang(Constans.Language)?.let {
            apiService.getHealthEntityPagedList(
                it,
                CityId,
                AreaId,
                HealthEntityTypeId,
                MaxResultCount,
                SkipCount
            )
        }

    suspend fun getPopularDoctors() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPopularDoctors(it) }

    suspend fun getDoctorHealthTopics() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getDoctorHealthTopics(it) }

    suspend fun getDoctorSpotLight() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getDoctorSpotLight(it) }

    suspend fun getPatientInfo() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPatientInfo(it) }

    suspend fun getPatientMedicalInfo() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPatientMedicalInfo(it) }

    suspend fun getAllInsurance_Companies() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllInsurance_Companies(it) }

    suspend fun getSalamtakGate() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getSalamtakGate(it) }

    suspend fun getBlogs(gateId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getBlogs(it, gateId) }

    suspend fun createQandAForPatient(
        questionRequest: MultipartBody
    ) = sp.getUserLang(Constans.Language)
        ?.let { apiService.createQandAForPatient(it, questionRequest) }

    suspend fun createPatient_Insurance(
        insuranceRequest: MultipartBody
    ) = sp.getUserLang(Constans.Language)
        ?.let { apiService.createPatient_Insurance(it, insuranceRequest) }

    suspend fun getPatient_Insurance(
    ) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPatient_Insurance(it) }
    suspend fun createDoctorRate(doctorRateRequest: HashMap<String, String>) = sp.getUserLang(Constans.Language)
        ?.let { apiService.createDoctorRate(it,doctorRateRequest) }

    suspend fun getPatientDetails(appointmentId :Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPatientDetails(it, appointmentId) }

    suspend fun editPatientAppointment(appointmentRequest: EditPatientAppointmentRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.editPatientAppointment(it, appointmentRequest) }
    suspend fun getPatientFavoritDoctor() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getPatientFavoritDoctor(it) }

    suspend fun getUserNotifications() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getUserNotifications(it) }
    suspend fun createPatientFavoritDoctor(favoriteRequest: FavoriteRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createPatientFavoritDoctor(it,favoriteRequest) }

    suspend fun getPatient() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getPatient(it) }

    suspend fun deleteUser() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.deleteUser(it) }

    suspend fun getDoctorReviews(doctorId :Int) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getDoctorReviews(it,doctorId) }

}