package com.korashiGroup.salamtakPatient.repository

import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.model.booking.EditPatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.NewDoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.medicalConsultant.MedicalConsultantReqBody
import com.korashiGroup.salamtakPatient.model.medicalConsultant.MedicalConsultantResponse
import com.korashiGroup.salamtakPatient.model.mobileClinic.MobileClinicReqBody
import com.korashiGroup.salamtakPatient.model.mobileClinic.MobileClinicResponse
import com.korashiGroup.salamtakPatient.model.notification.Response
import com.korashiGroup.salamtakPatient.model.nursingServices.NursingReqBody
import com.korashiGroup.salamtakPatient.model.nursingServices.NursingResponse
import com.korashiGroup.salamtakPatient.model.profile.LocationRequest
import com.korashiGroup.salamtakPatient.model.register.*
import com.korashiGroup.salamtakPatient.model.whatspresponse.WhatspRequest
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.SpUtil
import okhttp3.MultipartBody
import javax.inject.Inject

class RegisterRepositry @Inject constructor(private val apiService: ApiService) {
    @Inject
    lateinit var sp: SpUtil
    suspend fun login(culture: String, loginUser: LoginUser) = apiService.Login(culture, loginUser)
    suspend fun registerUser(registerUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.registerUser(it, registerUser) }

    suspend fun SendOTP(registerUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.SendOTP(it, registerUser) }
    suspend fun sendOTPWhatsApp(registerUser: WhatspRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.sendOTPWhatsApp(it, registerUser) }

    suspend fun createUser(createUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.createUser(it, createUser) }

    suspend fun createAnonymosPatient(createUser: CreateUser) =
        sp.getUserLang(Constans.Language)?.let { apiService.createAnonymosPatient(it, createUser) }

    suspend fun resetPassword(resetRequest: ResetRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.resetPassword(it, resetRequest) }

    suspend fun updatePassword(resetChangePassword: ResetChangePassword) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.updatePassword(it, resetChangePassword) }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.changePassword(it, changePasswordRequest) }

    suspend fun getHomeAds() =
        sp.getUserLang(Constans.Language)?.let { apiService.getHomeAds(it, 1, true) }

    suspend fun getCountries() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getCountries(it) }

    suspend fun getSpecialist() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getSpecialist(it) }

    suspend fun getTop10Specialist() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getTop10Specialist(it) }

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


    suspend fun getServiceLevel() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getServiceLevel(it) }

    suspend fun getAllCities() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllCities(it) }

    suspend fun getAreasByCityId(cityId: Int) =
        sp.getUserLang(Constans.Language)?.let { apiService.getAreasByCityId(it, cityId) }

    suspend fun searchDoctors(doctorSearchRequest: DoctorSearchRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.searchDoctors(it, doctorSearchRequest) }

    suspend fun doctorSearch3(doctorSearchRequest: NewDoctorSearchRequest) =
        sp.getUserLang(Constans.Language)?.let { apiService.doctorSearch3(it, doctorSearchRequest) }

    suspend fun getClinicSchedualByClinicDayId(
        ClinicId: Int, DayId: Int,
        MedicalExaminationTypeId: Int,
        BookDate: String
    ) = sp.getUserLang(Constans.Language)
        ?.let {
            apiService.getClinicSchedualByClinicDayId(
                it,
                ClinicId,
                DayId,
                MedicalExaminationTypeId,
                BookDate
            )
        }

    suspend fun getPolyClinicSchedualByClinicDayId(
        ClinicId: Int,
        doctorId: Int,
        dayId: Int,
        MedicalExaminationTypeId: Int
    ) = sp.getUserLang(Constans.Language)
        ?.let {
            apiService.getPolyClinicSchedualByClinicDayId(
                it,
                ClinicId,
                doctorId,
                dayId,
                MedicalExaminationTypeId
            )
        }

    suspend fun createPatientAppointment(appointmentRequest: PatientAppointmentRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createPatientAppointment(it, appointmentRequest) }

    suspend fun getDoctorProfileByDoctorId(clinicId: Int, DoctorId: Int) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getDoctorProfileByDoctorId(it, clinicId, DoctorId) }

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

    suspend fun getPopularDoctors(doctorRateRequest: HashMap<String, String>) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPopularDoctors(it,doctorRateRequest) }

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

    suspend fun createDoctorRate(doctorRateRequest: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createDoctorRate(it, doctorRateRequest) }

    suspend fun getPatientDetails(appointmentId: Int) = sp.getUserLang(Constans.Language)
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

    suspend fun updateNotification() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.updateNotification(it) }

    suspend fun createPatientFavoritDoctor(favoriteRequest: FavoriteRequest) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createPatientFavoritDoctor(it, favoriteRequest) }

    suspend fun getPatient() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getPatient(it) }

    suspend fun deleteUser() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.deleteUser(it) }

    suspend fun getDoctorReviews(doctorId: Int) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getDoctorReviews(it, doctorId) }

    suspend fun getPatientFawry() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getPatientFawry(it) }

    suspend fun getActiveOffers() =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getActiveOffers(it) }

    suspend fun getActiveOffers(OfferCategoryID: Int) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.Offers(it, OfferCategoryID) }

    suspend fun getFilteredHealthEntity(polyclinicRequest: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getFilteredHealthEntity(it, polyclinicRequest) }

    suspend fun getDoctorByClinicId(ClinicId: Int, SpecID: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getDoctorByClinicId(it, ClinicId, true, SpecID) }

    suspend fun getDoctorVideos(doctorId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getDoctorVideos(it, doctorId) }

    suspend fun getAllQandAByDoctorId() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllQandAByDoctorId(it) }

    suspend fun getAllQandA() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllQandA(it) }

    suspend fun getAnswersByDoctorId(doctorID: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAnswersByDoctorId(it, doctorID) }

    suspend fun checkUser() = sp.getUserLang(Constans.Language)
        ?.let { apiService.checkUser(it) }

    suspend fun getAllInvoicesForPatient() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllInvoicesForPatient(it) }

    suspend fun getSymptomsBySpecialityId(specialityId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getSymptomsBySpecialityId(it, specialityId) }

    suspend fun createPatientSymptoms(Request: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createPatientSymptoms(it, Request) }

    suspend fun getUnreadDoctorResponses() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getUnreadDoctorResponses(it) }

    suspend fun markAllDoctorResponses() = sp.getUserLang(Constans.Language)
        ?.let { apiService.markAllDoctorResponses(it) }

    suspend fun getServicesByType(typeId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getServicesByType(it, typeId) }

    suspend fun getLabsByTypeId(typeId: Int, labName: String? = null) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getLabsByTypeId(it, typeId, labName) }

    suspend fun getLabAvailabilityByLabId(labId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getLabAvailabilityByLabId(it, labId) }

    suspend fun getLabsByFilters(Request: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.getLabsByFilters(it, Request) }

    suspend fun getLabBranches(Request: HashMap<String, String>) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getLabBranches(it, Request) }

    suspend fun createLaboratoryReservation(Request: HashMap<String, String>) =
        sp.getUserLang(Constans.Language)
            ?.let { apiService.createLaboratoryReservation(it, Request) }

    suspend fun getPatientAppointments() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getPatientAppointments(it) }
    suspend fun getHomeCareService() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getHomeCareService(it) }

    suspend fun getLatestPatientAppointment() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getLatestPatientAppointment(it) }

    suspend fun getDoctorProfileByDoctorId(doctorId: Int) = sp.getUserLang(Constans.Language)
        ?.let { apiService.getDoctorProfileByDoctorId(it,doctorId) }
    suspend fun getAllPatientRelationshipType() = sp.getUserLang(Constans.Language)
        ?.let { apiService.getAllPatientRelationshipType(it) }
    suspend fun updateQandAIsNoShowByUser() = sp.getUserLang(Constans.Language)
        ?.let { apiService.updateQandAIsNoShowByUser(it) }

    // phase 2

    suspend fun sendMedicalConsultant(fullName: String,
                                      mobileNumber: String,
                                      specialityId: Int,
                                      symptoms: String,
                                      question: String): retrofit2.Response<MedicalConsultantResponse> {
        val requestBody = MedicalConsultantReqBody(
            FullName = fullName,
            MobileNumber = mobileNumber,
            SpecialityId = specialityId,
            Symptoms = symptoms,
            Question = question
        )
        sp.getUserLang(Constans.Language).let {
            return apiService.sendMedicalConsultant(
                culture = it,
                medicalConsultantRequest = requestBody
            )
        }


    }


    suspend fun sendMobileClinic(fullName: String,
                                 mobileNumber: String,
                                 specialityId: Int,
                                 buildingNumber: String,
                                 floor: String,
                                 apartment: String,
                                 street: String,
                                 cityId: Int,
                                 areaId: Int,
                                 subSpecialistyId: Int,
                                 seniorityLevelId: Int,
                                 sepcialRecuirements: String,
                                 genderId: Int,
                                 symptoms: String
                                 ): retrofit2.Response<MobileClinicResponse> {
        val requestBody = MobileClinicReqBody(
            FullName = fullName,
            MobailPhone = mobileNumber,
            SpecialityId = specialityId,
            Symptoms = symptoms,
            BuildingNumber = buildingNumber,
            Floor = floor,
            Aparment = apartment,
            Street = street,
            CityId = cityId,
            AreaId = areaId,
            SubSpecialistyId = subSpecialistyId,
            SeniorityLevelId = seniorityLevelId,
            SepcialRecuirements = sepcialRecuirements,
            GenderId = genderId
        )
        sp.getUserLang(Constans.Language).let {
            return apiService.sendMobileClinic(
                culture = it,
                mobileClinicRequest = requestBody
            )
        }


    }

    suspend fun sendNursingService(fullName: String,
                                 mobileNumber: String,
                                 buildingNumber: String,
                                 street: String,
                                 floor: String,
                                 apartment: String,
                                 cityId: Int,
                                 areaId: Int,
                                 genderId: Int,
                                 fromDay:String,
                                 toDay:String,
                                 fromHour:String,
                                 toHour:String,
                                 serviceId:Int,
                                 service:String
    ): retrofit2.Response<NursingResponse> {
        val requestBody = NursingReqBody(
            FullName = fullName,
            MobailPhone = mobileNumber,
            BuildingNumber = buildingNumber,
            Street = street,
            Floor = floor,
            Apartment = apartment,
            CityId = cityId,
            AreaId = areaId,
            GenderId = genderId,
            FromDay = fromDay,
            ToDay = toDay,
            FromHour = fromHour,
            ToHour = toHour,
            ServiceId = serviceId,
            Service = service
        )
        sp.getUserLang(Constans.Language).let {
            return apiService.sendNursingServiceReq(
                culture = it,
                nursingServicesReq = requestBody
            )
        }


    }




}