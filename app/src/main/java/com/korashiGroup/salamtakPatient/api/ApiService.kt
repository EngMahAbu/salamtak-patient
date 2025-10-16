package com.korashiGroup.salamtakPatient.api

import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicResponse
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsDoctorsResponse
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.askandanswer.QuestionAndAnswerResponse
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.AnswersDoctorResponse
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.QuestionsResponse
import com.korashiGroup.salamtakPatient.model.booking.*
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.booking.doctorprofile.DoctorProfileResponse
import com.korashiGroup.salamtakPatient.model.checktoken.CheckTokenResponse
import com.korashiGroup.salamtakPatient.model.deleteuser.DeleteUserResponse
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorsearchItemResponse
import com.korashiGroup.salamtakPatient.model.docotorsearch.NewDoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.emr.EmrPatientResponse
import com.korashiGroup.salamtakPatient.model.favorite.AddToFavoriteResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteDoctorResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.fawry.FawryResponse
import com.korashiGroup.salamtakPatient.model.healthy.GetHealthEntityResponse
import com.korashiGroup.salamtakPatient.model.homecare.GetHomeCareServicesResponse
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCardResponse
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompanyResponse
import com.korashiGroup.salamtakPatient.model.insurance.SendInsuranceCardResponce
import com.korashiGroup.salamtakPatient.model.invoice.DoctorInvoiceResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.AnalysisResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.CalenderResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabAppointementResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabReservationResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabsBranchesResponse
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabsResponse
import com.korashiGroup.salamtakPatient.model.notification.DoctorNotificationResponse
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffersResponse
import com.korashiGroup.salamtakPatient.model.offers.OffersDetailsResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.GetDoctorHealthTopicsResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.GetDoctorSpotLightResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.NewPopularDoctorResponse
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorsResponseItem
import com.korashiGroup.salamtakPatient.model.profile.*
import com.korashiGroup.salamtakPatient.model.rating.RatingResponse
import com.korashiGroup.salamtakPatient.model.register.*
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.BlogsResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.SalamtakGateResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.blogs.GetBlogsResponse
import com.korashiGroup.salamtakPatient.model.scheduling.CancelPatientAppointmentResponse
import com.korashiGroup.salamtakPatient.model.scheduling.GetLatestPatientAppointmentResponse
import com.korashiGroup.salamtakPatient.model.scheduling.GetPatientAppointmentesResponse
import com.korashiGroup.salamtakPatient.model.syemtoms.Syemtoms
import com.korashiGroup.salamtakPatient.model.syemtoms.SymetomsResponse
import com.korashiGroup.salamtakPatient.notification.NotificationListResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("{culture}/User/Login")
    suspend fun Login(@Path("culture") culture: String,
                             @Body loginUser: LoginUser):Response<LoginResponse>
    @POST("{culture}/User/Register")
    suspend fun registerUser(@Path("culture") culture: String,
                           @Body registerUser: CreateUser):Response<RegisterReponse>

    @POST("{culture}/User/Register")
    suspend fun SendOTP(@Path("culture") culture: String,
                        @Body registerUser: CreateUser):Response<RegisterReponse>

    @POST("{culture}/User/CreateUser")
    suspend fun createUser(@Path("culture") culture: String,
                             @Body createUser: CreateUser):Response<UserResponse>
    @POST("{culture}/User/CreateAnonymosPatient")
    suspend fun createAnonymosPatient(@Path("culture") culture: String,
                           @Body createUser: CreateUser):Response<UserResponse>

    @GET("{culture}/LookUp/GetCountries")
    suspend fun getCountries(
        @Path("culture") culture: String,
    ): Response<GetCountry>

    @GET("{culture}/Ads/GetVidoesAds")
    suspend fun getHomeAds(
        @Path("culture") culture: String,
        @Query("PageID") PageID: Int,
        @Query("DoctorApp") DoctorApp: Boolean
    ): Response<HomeAdsResponse>

    @GET("{culture}/Specialist/GetSpecialist")
    suspend fun getSpecialist(
        @Path("culture") culture: String,
    ): Response<GetSpecialistResponse>
    @GET("{culture}/Specialist/GetTop10Specialist")
    suspend fun getTop10Specialist(
        @Path("culture") culture: String,
    ): Response<GetSpecialistResponse>

    @GET("{culture}/LookUp/GetMedicalExaminationType")
    suspend fun getMedicalExamination(
        @Path("culture") culture: String,
    ): Response<MedicalExaminationResponse>

    @GET("{culture}/Specialist/GetSubSpecialist")
    suspend fun getSubSpecialist(
        @Path("culture") culture: String,
        @Query("specialListId") specialListId:Int,
        @Query("UsertypeID") userTypeID:Int=3,
    ): Response<GetSubSpecilistResponse>

    @GET("{culture}/PatientLookUp/GetBloodTypes")
    suspend fun getBloodTypes(
        @Path("culture") culture: String,
    ): Response<BloodTypeResponse>

    @GET("{culture}/PatientLookUp/GetMedicineAllergy")
    suspend fun getMedicineAllergy(
        @Path("culture") culture: String,
    ): Response<MedicineAllergyResponse>

    @GET("{culture}/PatientLookUp/GetFoodAllergy")
    suspend fun getFoodAllergy(
        @Path("culture") culture: String,
    ): Response<FoodAllergyResponse>


    @GET("{culture}/SeniorityLevel/GetSeniorityLevel")
    suspend fun getSeniorityLevel(
        @Path("culture") culture: String,
    ): Response<GetSeniorityLevelResponse>

    @GET("{culture}/City/GetAllCities")
    suspend fun getAllCities(
        @Path("culture") culture: String,
    ): Response<GetAllCitiesResponse>

    @GET("{culture}/Area/GetAreasByCityId")
    suspend fun getAreasByCityId(
        @Path("culture") culture: String,
        @Query("cityId") cityId:Int,
    ): Response<GetAreasByCityIdResponse>

    @POST("{culture}/DoctorSearch/DoctorSearch")
    suspend fun searchDoctors(
        @Path("culture") culture: String,
        @Body doctorSearchRequest:DoctorSearchRequest
    ): Response<DoctorsearchItemResponse>

    @POST("{culture}/DoctorSearch/DoctorSearch3")
    suspend fun doctorSearch3(
        @Path("culture") culture: String,
        @Body doctorSearchRequest: NewDoctorSearchRequest
    ): Response<DoctorsearchItemResponse>

    @GET("{culture}/DoctorClinic/GetClinicSchedualByClinicDayId")
    suspend fun getClinicSchedualByClinicDayId(
        @Path("culture") culture: String,
        @Query("ClinicId") ClinicId :Int,
        @Query("DayId") DayId  :Int,
        @Query("MedicalExaminationTypeId")
        MedicalExaminationTypeId :Int,
        @Query("BookDate") BookDate :String,
    ): Response<BookingResponse>

    @GET("{culture}/DoctorClinic/GetPolyClinicSchedualByClinicDayId")
    suspend fun getPolyClinicSchedualByClinicDayId(
        @Path("culture") culture: String,
        @Query("ClinicId") ClinicId :Int,
        @Query("DoctorID") DoctorID :Int,
        @Query("DayId") DayId  :Int,
        @Query("MedicalExaminationTypeId")
        MedicalExaminationTypeId :Int,
    ): Response<BookingResponse>
    @POST("{culture}/Patient/CreatePatientAppointment")
    suspend fun createPatientAppointment(
        @Path("culture") culture: String,
        @Body appointmentRequest: PatientAppointmentRequest
    ): Response<CreatepatientAppointementsResponse>

    @GET("{culture}/Doctor/GetDoctorProfileByDoctorIdAndClinicID")
    suspend fun getDoctorProfileByDoctorId(
        @Path("culture") culture: String,
        @Query("ClinicId") ClinicId :Int,
        @Query("DoctorId") DoctorId :Int,
    ): Response<GetDoctorClinksResponse>

    @GET("{culture}/PatientAppointment/GetPatientAppointmentes")
    suspend fun getPatientAppointmentes(
        @Path("culture") culture: String,
      @Query("medicalExaminationTypeId") medicalExaminationTypeId :Int?=null,
    ): Response<GetPatientAppointmentesResponse>

    @GET("{culture}/PatientAppointment/CancelPatientAppointment")
    suspend fun cancelPatientAppointment(
        @Path("culture") culture: String,
        @Query("AppointmentId") AppointmentId :Int,
    ): Response<CancelPatientAppointmentResponse>

    @POST("{culture}/Patient/CreatePatientProfileFirstStep")
    suspend fun createPatientProfile(
        @Path("culture") culture: String,
        @Body part: RequestBody
    ): Response<PatientProfileResponse>

    @POST("{culture}/Patient/updatePatientProfileFirstStep")
    suspend fun updatePatientProfile(
        @Path("culture") culture: String,
        @Body part: RequestBody
    ): Response<UpdateProfilePatientResponse>

    @POST("{culture}/Patient/CreateAndUpdatePatientProfileSecondStep")
    suspend fun sendPatientLocation(
        @Path("culture") culture: String,
        @Body locationRequest: LocationRequest
    ): Response<PatientLocationResponse>




    @POST("{culture}/User/ResetPassword")
    suspend fun resetPassword(
        @Path("culture") culture: String,
        @Body request: ResetRequest
    ): Response<ResetResponse>

    @POST("{culture}/User/UpdatePassword")
    suspend fun updatePassword(
        @Path("culture") culture: String,
        @Body resetChangePassword: ResetChangePassword
    ): Response<UpdatePasswordResponse>


    @POST("{culture}/User/ChangePassword")
    suspend fun changePassword(
        @Path("culture") culture: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @GET("{culture}/HealthEntity/GetHealthEntityPagedList")
    suspend fun getHealthEntityPagedList(
        @Path("culture") culture: String,
        @Query("CityId") CityId :Int,
        @Query("AreaId") AreaId :Int,
        @Query("HealthEntityTypeId") HealthEntityTypeId :Int,
        @Query("MaxResultCount") MaxResultCount :Int,
        @Query("SkipCount") SkipCount :Int,
    ): Response<GetHealthEntityResponse>

    @POST("{culture}/Doctor/GetPopularDoctors")
    suspend fun getPopularDoctors(
        @Path("culture") culture: String,
        @Body doctorRateRequest: Map<String, String>,
    ):Response<NewPopularDoctorResponse>

    @GET("{culture}/LookUp/GetDoctorSpotLight")
    suspend fun getDoctorSpotLight(
        @Path("culture") culture: String,
    ):Response<GetDoctorSpotLightResponse>

    @GET("{culture}/LookUp/GetDoctorHealthTopics")
    suspend fun getDoctorHealthTopics(
        @Path("culture") culture: String,
    ):Response<GetDoctorHealthTopicsResponse>

    @GET("{culture}/Patient/GetPatient")
    suspend fun getPatientInfo(
        @Path("culture") culture: String,
    ):Response<GetPatientInfoResponse>

    @GET("{culture}/Patient/GetPatientMedicalInfo")
    suspend fun getPatientMedicalInfo(
        @Path("culture") culture: String,
    ):Response<PatientMedicalInfo>

    @GET("{culture}/Insurance_Companies/GetAllInsurance_Companies")
    suspend fun getAllInsurance_Companies(
        @Path("culture") culture: String,
    ):Response<InsuranceCompanyResponse>


    @GET("{culture}/LookUp/GetSalamtakGate")
    suspend fun getSalamtakGate(
        @Path("culture") culture: String,
    ):Response<SalamtakGateResponse>

    @GET("{culture}/LookUp/GetBlogs")
    suspend fun getBlogs(
        @Path("culture") culture: String,
        @Query("salamtakGateEnum") salamtakGateEnum :Int,
    ):Response<GetBlogsResponse>



    @POST("{culture}/Patient/CreateQandAForPatient")
    suspend fun createQandAForPatient(
        @Path("culture") culture: String,
        @Body part: RequestBody
    ): Response<QuestionAndAnswerResponse>

    @POST("{culture}/Insurance_Companies/CreatePatient_Insurance")
    suspend fun createPatient_Insurance(
        @Path("culture") culture: String,
        @Body part: RequestBody
    ): Response<SendInsuranceCardResponce>


    @GET("{culture}/Insurance_Companies/GetPatient_Insurance")
    suspend fun getPatient_Insurance(
        @Path("culture") culture: String,
    ):Response<InsuranceCardResponse>

    @POST("{culture}/Patient/CreatePatientMedicallInfo")
    suspend fun CreatePatientMedicallInfo(
        @Path("culture") culture: String,
        @Body medicalInfoRequest: Map<String, String>,
    ): Response<PatientMedicalInfoResponse>

    @POST("{culture}/DoctorRate/CreateDoctorRate")
    suspend fun createDoctorRate(
        @Path("culture") culture: String,
        @Body doctorRateRequest: Map<String, String>,
    ): Response<RatingResponse>

    @GET("{culture}/Patient/GetPatientEmrDetails")
    suspend fun getPatientDetails(
        @Path("culture") culture: String,
        @Query("appointmentId") appointmentId :Int,
    ):Response<EmrPatientResponse>


    @POST("{culture}/PatientAppointment/EditPatientAppointment'")
    suspend fun editPatientAppointment(
        @Path("culture") culture: String,
        @Body appointmentRequest: EditPatientAppointmentRequest
    ): Response<CreatepatientAppointementsResponse>

    @GET("{culture}/Patient/GetPatientFavoritDoctor")
    suspend fun getPatientFavoritDoctor(
        @Path("culture") culture: String,
    ):Response<FavoriteDoctorResponse>

    @GET("{culture}/Notification/GetUserNotifications")
    suspend fun getUserNotifications(
        @Path("culture") culture: String,
    ):Response<NotificationListResponse>
    @POST("{culture}/User/UpdateNotification")
    suspend fun updateNotification(
        @Path("culture") culture: String,
    ): Response<NotificationListResponse>

    @POST("{culture}/Patient/CreatePatientFavoritDoctor")
    suspend fun createPatientFavoritDoctor(
        @Path("culture") culture: String,
        @Body favoriteRequest: FavoriteRequest
    ): Response<AddToFavoriteResponse>


    @GET("{culture}/Patient/GetPatient")
    suspend fun getPatient(
        @Path("culture") culture: String,
    ):Response<GetPatientProfileResponse>
    @DELETE("{culture}/User/DeleteUser")
    suspend fun deleteUser(
        @Path("culture") culture: String,
    ):Response<DeleteUserResponse>

    @GET("{culture}/DoctorRate/GetDoctorReviews")
    suspend fun getDoctorReviews(
        @Path("culture") culture: String,
        @Query("doctorId") doctorId :Int,
    ):Response<ReviewsResponse>
    @GET("{culture}/LookUp/GetPatient_Fawry_Setting")
    suspend fun getPatientFawry(
        @Path("culture") culture: String,
    ):Response<FawryResponse>
    @GET("{culture}/Offers/GetActiveOffers")
    suspend fun getActiveOffers(
        @Path("culture") culture: String,
    ):Response<ActiveOffersResponse>
    @GET("{culture}/Offers/GetActiveOffersDetails")
    suspend fun Offers(
        @Path("culture") culture: String,
        @Query("OfferCategoryID") OfferCategoryID :Int,
    ):Response<OffersDetailsResponse>

    @POST("{culture}/HealthEntity/GetFilteredPolyClinics")
    suspend fun getFilteredHealthEntity(
        @Path("culture") culture: String,
        @Body polyclinicRequest: Map<String, String>,
    ): Response<PolyClinicResponse>
    @GET("{culture}/Doctor/GetDoctorByClinicId")
    suspend fun getDoctorByClinicId(
        @Path("culture") culture: String,
        @Query("ClinicId") ClinicId :Int,
        @Query("IsApproved") IsApproved :Boolean=true,
        @Query("SpecID") SpecID :Int,
    ): Response<PolyClinicsDoctorsResponse>
    @GET("{culture}/Doctor/GetDoctorVideos")
    suspend fun getDoctorVideos(
        @Path("culture") culture: String,
        @Query("DoctorID") doctorID :Int,
    ): Response<PolyClinicsDoctorsResponse>
    @GET("{culture}/Patient/GetAllQandAByPatientId")
    suspend fun getAllQandAByDoctorId(
        @Path("culture") culture: String,
    ): Response<QuestionsResponse>
    @GET("{culture}/LookUp/GetAllQandA")
    suspend fun getAllQandA(
        @Path("culture") culture: String,
    ): Response<QuestionsResponse>
    @GET("{culture}/Doctor/GetAllQandAByDoctorId")
    suspend fun getAnswersByDoctorId(
        @Path("culture") culture: String,
        @Query("DoctorID") doctorID :Int,
    ): Response<AnswersDoctorResponse>
    @GET("{culture}/User/CheckToken")
    suspend fun checkUser(
        @Path("culture") culture: String,
    ): Response<CheckTokenResponse>
    @GET("{culture}/Patient/GetAllInvoicesForPatient")
    suspend fun getAllInvoicesForPatient(
        @Path("culture") culture: String,
    ): Response<DoctorInvoiceResponse>
    @GET("{culture}/Patient/GetSymptomsBySpecialityId")
    suspend fun getSymptomsBySpecialityId(
        @Path("culture") culture: String,
        @Query("specialityId") specialityId :Int,
    ): Response<SymetomsResponse>
    @POST("{culture}/Patient/CreatePatientSymptoms")
    suspend fun createPatientSymptoms(
        @Path("culture") culture: String,
        @Body medicalInfoRequest: Map<String, String>,
    ): Response<Syemtoms>
    @POST("{culture}/Patient/MarkAllDoctorResponses")
    suspend fun markAllDoctorResponses(
        @Path("culture") culture: String,
    ): Response<DoctorNotificationResponse>

    @GET("{culture}/Patient/GetUnreadDoctorResponses")
    suspend fun getUnreadDoctorResponses(
        @Path("culture") culture: String,
    ): Response<DoctorNotificationResponse>
    @GET("{culture}/HealthEntity/GetServicesByType")
    suspend fun getServicesByType(
        @Path("culture") culture: String,
        @Query("typeId") typeId :Int,
    ): Response<AnalysisResponse>
    @GET("{culture}/HealthEntity/GetLabsByTypeId")
    suspend fun getLabsByTypeId(
        @Path("culture") culture: String,
        @Query("typeId") typeId :Int,
        @Query("labName") labName :String?=null,
    ): Response<LabsResponse>
    @GET("{culture}/HealthEntity/GetLabAvailabilityByLabId")
    suspend fun getLabAvailabilityByLabId(
        @Path("culture") culture: String,
        @Query("labId") labId :Int,
    ): Response<CalenderResponse>
    @POST("{culture}/HealthEntity/GetLabsByFilters")
    suspend fun getLabsByFilters(
        @Path("culture") culture: String,
        @Body labsRequest: Map<String, String>,
    ): Response<LabsBranchesResponse>

    @POST("{culture}/HealthEntity/GetLabBranches")
    suspend fun getLabBranches(
        @Path("culture") culture: String,
        @Body labsRequest: Map<String, String>,
    ): Response<LabsBranchesResponse>
    @POST("{culture}/HealthEntity/CreateLaboratoryReservation")
    suspend fun createLaboratoryReservation(
        @Path("culture") culture: String,
        @Body analysisLabRequest: Map<String, String>,
    ): Response<LabReservationResponse>

    @GET("{culture}/HealthEntity/GetPatientAppointments")
    suspend fun getPatientAppointments(
        @Path("culture") culture: String,
    ): Response<LabAppointementResponse>

    // home care apis
    @GET("{culture}/LookUp/GetHomeService")
    suspend fun getHomeCareService(
        @Path("culture") culture: String,
    ): Response<GetHomeCareServicesResponse>
 @GET("{culture}/PatientAppointment/GetLatestPatientAppointment")
    suspend fun getLatestPatientAppointment(
        @Path("culture") culture: String,
    ): Response<GetLatestPatientAppointmentResponse>

    @GET("{culture}/Doctor/GetDoctorProfileByDoctorId")
    suspend fun getDoctorProfileByDoctorId(
        @Path("culture") culture: String,
        @Query("DoctorId") DoctorId :Int,
    ): Response<DoctorProfileResponse>

}