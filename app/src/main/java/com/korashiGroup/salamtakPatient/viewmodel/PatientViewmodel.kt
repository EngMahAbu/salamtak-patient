package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.booking.*
import com.korashiGroup.salamtakPatient.model.profile.*
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.model.deleteuser.DeleteUserResponse
import com.korashiGroup.salamtakPatient.model.emr.EmrPatientResponse
import com.korashiGroup.salamtakPatient.model.favorite.AddToFavoriteResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteDoctorResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.fawry.FawryResponse
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffersResponse
import com.korashiGroup.salamtakPatient.model.offers.OffersDetailsResponse
import com.korashiGroup.salamtakPatient.model.rating.RatingResponse
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsResponse
import com.korashiGroup.salamtakPatient.notification.NotificationListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PatientViewmodel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val doctorRateResponse=MutableLiveData<Resource<RatingResponse>>()
    val emrResponse=MutableLiveData<Resource<EmrPatientResponse>>()
    val editAppointmentResponse=MutableLiveData<Resource<CreatepatientAppointementsResponse>>()
    val favoriteDoctorResponse=MutableLiveData<Resource<FavoriteDoctorResponse>>()
    val notificationResponse=MutableLiveData<Resource<NotificationListResponse>>()
    val updateNotificationResponse=MutableLiveData<Resource<NotificationListResponse>>()
    val favoriteResponse=MutableLiveData<Resource<AddToFavoriteResponse>>()
    val profileResponse=MutableLiveData<Resource<GetPatientProfileResponse>>()
    val deleteUserResponse=MutableLiveData<Resource<DeleteUserResponse>>()
    val reviewsResponse=MutableLiveData<Resource<ReviewsResponse>>()
    val fawryResponse=MutableLiveData<Resource<FawryResponse>>()
    val activeOfferResponse=MutableLiveData<Resource<ActiveOffersResponse>>()
    val offerResponse=MutableLiveData<Resource<OffersDetailsResponse>>()

    fun createDoctorRate(doctorRateRequest: HashMap<String, String> )=viewModelScope.launch(Dispatchers.IO+handler) {
        doctorRateResponse.postValue(Resource.Loading())
        val response=repositry.createDoctorRate(doctorRateRequest)
        doctorRateResponse.postValue(response?.let { handlecreateDoctorRate(it) })
    }

    private fun handlecreateDoctorRate(response: Response<RatingResponse>): Resource<RatingResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<RatingResponse>(response.errorBody()!!.string(), RatingResponse::class.java)
        return error.Message?.let { Resource.Error(it) }
    }

    fun getPatientDetails( appointmentId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        emrResponse.postValue(Resource.Loading())
        val response=repositry.getPatientDetails(appointmentId)
        emrResponse.postValue(response?.let { handleGetPatientDetails(it) })
    }

    private fun handleGetPatientDetails(response: Response<EmrPatientResponse>): Resource<EmrPatientResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<EmrPatientResponse>(response.errorBody()!!.string(), EmrPatientResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getEditPatientAppointment(appointmentRequest: EditPatientAppointmentRequest)=viewModelScope.launch(Dispatchers.IO+handler) {
        editAppointmentResponse.postValue(Resource.Loading())
        val response=repositry.editPatientAppointment(appointmentRequest)
        editAppointmentResponse.postValue(response?.let { handlegetEditPatientAppointment(it) })
    }

    private fun handlegetEditPatientAppointment(response: Response<CreatepatientAppointementsResponse>): Resource<CreatepatientAppointementsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<CreatepatientAppointementsResponse>(response.errorBody()!!.string(), CreatepatientAppointementsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    fun getPatientFavoritDoctor()=viewModelScope.launch(Dispatchers.IO+handler) {
        favoriteDoctorResponse.postValue(Resource.Loading())
        val response=repositry.getPatientFavoritDoctor()
        favoriteDoctorResponse.postValue(response?.let { handlegetPatientFavoritDoctor(it) })
    }

    private fun handlegetPatientFavoritDoctor(response: Response<FavoriteDoctorResponse>): Resource<FavoriteDoctorResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<FavoriteDoctorResponse>(response.errorBody()!!.string(), FavoriteDoctorResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun getUserNotifications()=viewModelScope.launch(Dispatchers.IO+handler) {
        notificationResponse.postValue(Resource.Loading())
        val response=repositry.getUserNotifications()
        notificationResponse.postValue(response?.let { handlegetUserNotifications(it) })
    }

    private fun handlegetUserNotifications(response: Response<NotificationListResponse>): Resource<NotificationListResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<NotificationListResponse>(response.errorBody()!!.string(), NotificationListResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun updateNotification()=viewModelScope.launch(Dispatchers.IO+handler) {
        updateNotificationResponse.postValue(Resource.Loading())
        val response=repositry.updateNotification()
        updateNotificationResponse.postValue(response?.let { handleUpdateNotification(it) })
    }

    private fun handleUpdateNotification(response: Response<NotificationListResponse>): Resource<NotificationListResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<NotificationListResponse>(response.errorBody()!!.string(), NotificationListResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    fun createPatientFavoriteDoctor(favoriteRequest: FavoriteRequest)=viewModelScope.launch(Dispatchers.IO+handler) {
        favoriteResponse.postValue(Resource.Loading())
        val response=repositry.createPatientFavoritDoctor(favoriteRequest)
        favoriteResponse.postValue(response?.let { handlecreatePatientFavoriteDoctor(it) })
    }

    private fun handlecreatePatientFavoriteDoctor(response: Response<AddToFavoriteResponse>): Resource<AddToFavoriteResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<AddToFavoriteResponse>(response.errorBody()!!.string(), AddToFavoriteResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientProfileByPateintId()=viewModelScope.launch(Dispatchers.IO+handler) {
        profileResponse.postValue(Resource.Loading())
        val response=repositry.getPatient()
        profileResponse.postValue(response?.let { handlegetPatientProfileByPateintId(it) })
    }

    private fun handlegetPatientProfileByPateintId(response: Response<GetPatientProfileResponse>): Resource<GetPatientProfileResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<GetPatientProfileResponse>(response.errorBody()!!.string(), GetPatientProfileResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun deleteUser()=viewModelScope.launch(Dispatchers.IO+handler) {
        deleteUserResponse.postValue(Resource.Loading())
        val response=repositry.deleteUser()
        deleteUserResponse.postValue(response?.let { handledeleteUser(it) })
    }

    private fun handledeleteUser(response: Response<DeleteUserResponse>): Resource<DeleteUserResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<DeleteUserResponse>(response.errorBody()!!.string(), DeleteUserResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }



    fun getDoctorReviews(doctorId :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
        reviewsResponse.postValue(Resource.Loading())
        val response=repositry.getDoctorReviews(doctorId)
        reviewsResponse.postValue(response?.let { handlegetDoctorReviews(it) })
    }

    private fun handlegetDoctorReviews(response: Response<ReviewsResponse>): Resource<ReviewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<ReviewsResponse>(response.errorBody()!!.string(), ReviewsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getPatientFawry()=viewModelScope.launch(Dispatchers.IO+handler) {
        fawryResponse.postValue(Resource.Loading())
        val response=repositry.getPatientFawry()
        fawryResponse.postValue(response?.let { handleGetPatientFawry(it) })
    }

    private fun handleGetPatientFawry(response: Response<FawryResponse>): Resource<FawryResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<FawryResponse>(response.errorBody()!!.string(), FawryResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getActiveOffers()=viewModelScope.launch(Dispatchers.IO+handler) {
        activeOfferResponse.postValue(Resource.Loading())
        val response=repositry.getActiveOffers()
        activeOfferResponse.postValue(response?.let { handleGetActiveOffers(it) })
    }

    private fun handleGetActiveOffers(response: Response<ActiveOffersResponse>): Resource<ActiveOffersResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<ActiveOffersResponse>(response.errorBody()!!.string(), ActiveOffersResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun getOffers(OfferCategoryID :Int)=viewModelScope.launch(Dispatchers.IO+handler) {
       offerResponse.postValue(Resource.Loading())
        val response=repositry.getActiveOffers(OfferCategoryID)
        offerResponse.postValue(response?.let { handleGetOffers(it) })
    }

    private fun handleGetOffers(response: Response<OffersDetailsResponse>): Resource<OffersDetailsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<OffersDetailsResponse>(response.errorBody()!!.string(), OffersDetailsResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }




    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","exception${exception.message.toString()}")
    }
}