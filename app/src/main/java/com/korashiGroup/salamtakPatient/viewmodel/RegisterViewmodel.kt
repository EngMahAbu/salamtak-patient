package com.korashiGroup.salamtakPatient.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.HomeAdsResponse
import com.korashiGroup.salamtakPatient.model.register.*
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import com.korashiGroup.salamtakPatient.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewmodel @Inject constructor(private val repositry: RegisterRepositry):ViewModel(){


    val response=MutableLiveData<Resource<RegisterReponse>>()
    val otpResponse=MutableLiveData<Resource<RegisterReponse>>()
    val createResponse=MutableLiveData<Resource<UserResponse>>()
    val loginResponse=MutableLiveData<Resource<LoginResponse>>()
    val imagevedioresponse=MutableLiveData<Resource<HomeAdsResponse>>()
    val resetPasswordesponse=MutableLiveData<Resource<ResetResponse>>()
    val updatePasswordesponse=MutableLiveData<Resource<UpdatePasswordResponse>>()
    val changePasswordesponse=MutableLiveData<Resource<ChangePasswordResponse>>()

    fun registerUser(createUser: CreateUser)=viewModelScope.launch(Dispatchers.IO+handler)  {

        response.postValue(Resource.Loading())
        val registerResponse=repositry.registerUser(createUser)
        response.postValue(registerResponse?.let { handleRegister(it) })
    }



    fun loginUser(culture:String,loginUser: LoginUser) = viewModelScope.launch(Dispatchers.IO + handler) {
        loginResponse.postValue(Resource.Loading())

        try {
            val response=repositry.login(culture,loginUser)

            if (response.isSuccessful) {
                response.body()?.let {
                    loginResponse.postValue(Resource.sucess(it))
                } ?: loginResponse.postValue(Resource.Error("الاستجابة من الخادم فارغة"))
            } else {

                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("message", "حدث خطأ غير معروف")
                } catch (e: Exception) {
                    "حدث خطأ غير معروف"
                }

                Log.e("HTTP Error", "Code: ${response.code()}, message: $errorMessage")
                loginResponse.postValue(Resource.Error(errorMessage))
            }

        } catch (e: Exception) {
            Log.e("exception", "Exception: ${e.message}")

            val userMessage = when (e) {
                is java.net.UnknownHostException -> "تعذر الاتصال بالخادم، تحقق من اتصال الإنترنت."
                is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال، حاول مرة أخرى لاحقاً."
                is javax.net.ssl.SSLHandshakeException -> "حدث خطأ في الاتصال الآمن (SSL)."
                else -> e.message ?: "حدث خطأ غير معروف"
            }

            loginResponse.postValue(Resource.Error(userMessage))
        }
    }




//    fun loginUser(culture:String,loginUser: LoginUser)=viewModelScope.launch(Dispatchers.IO+handler) {
//        loginResponse.postValue(Resource.Loading())
//        val response=repositry.login(culture,loginUser)
//        loginResponse.postValue(handleLogin(response))
//    }

    private fun handleLogin(response: Response<LoginResponse>): Resource<LoginResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
               return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<LoginResponse>(response.errorBody()!!.string(), LoginResponse::class.java)
        return Resource.Error(error.message)
    }

    fun Sentotp(registerUser: CreateUser)=viewModelScope.launch(Dispatchers.IO+handler) {

        otpResponse.postValue(Resource.Loading())
        val response=repositry.SendOTP(registerUser)
        otpResponse.postValue(response?.let { handleOTP(it) })
    }

    fun createuser(createUser: CreateUser)=viewModelScope.launch(Dispatchers.IO+handler)  {

        createResponse.postValue(Resource.Loading())
        val response=repositry.createUser(createUser)
        createResponse.postValue(response?.let { handleCreateUser(it) })
    }

    private fun handleCreateUser(response: Response<UserResponse>): Resource<UserResponse>? {
        if (response.isSuccessful){
            response.body()?.let { registerResponse->
                return Resource.sucess(registerResponse)
            }

        }
        val error = Gson().fromJson<UserResponse>(response.errorBody()!!.string(), UserResponse::class.java)
        return Resource.Error(error.message)

    }

    fun createAnonymosPatient(createUser: CreateUser)=viewModelScope.launch(Dispatchers.IO+handler)  {

        createResponse.postValue(Resource.Loading())
        val response=repositry.createAnonymosPatient(createUser)
        createResponse.postValue(response?.let { handleCreateAnonymosPatient(it) })
    }

    private fun handleCreateAnonymosPatient(response: Response<UserResponse>): Resource<UserResponse>? {
        if (response.isSuccessful){
            response.body()?.let { registerResponse->
                return Resource.sucess(registerResponse)
            }

        }
        val error = Gson().fromJson<UserResponse>(response.errorBody()!!.string(), UserResponse::class.java)
        return Resource.Error(error.message)

    }

    private fun handleRegister(response: Response<RegisterReponse>): Resource<RegisterReponse>?{

        if (response.isSuccessful){
            response.body()?.let { registerResponse ->
                return Resource.sucess(registerResponse)
            }

        }

        val error = Gson().fromJson<RegisterReponse>(response.errorBody()!!.string(), RegisterReponse::class.java)
        return Resource.Error(error.message)

    }

    private fun handleOTP(otpResponse: Response<RegisterReponse>): Resource<RegisterReponse>?{

        if (otpResponse.isSuccessful){
            otpResponse.body()?.let { registerResponse->
                return Resource.sucess(registerResponse)
            }

        }
        return Resource.Error(otpResponse.message())

    }


    fun getHomeAdds()=viewModelScope.launch(Dispatchers.IO+handler)  {

        imagevedioresponse.postValue(Resource.Loading())
        val imageVedioResponse=repositry.getHomeAds()
        imagevedioresponse.postValue(imageVedioResponse?.let { handleImageVedio(it) })
    }

    private fun handleImageVedio(imageVedioResponse: Response<HomeAdsResponse>): Resource<HomeAdsResponse>? {
        if (imageVedioResponse.isSuccessful){
            imageVedioResponse.body()?.let { it->
                return Resource.sucess(it)
            }
        }
        return Resource.Error(imageVedioResponse.message())
    }


    fun resetPassword(resetRequest: ResetRequest)=viewModelScope.launch(Dispatchers.IO+handler) {
        resetPasswordesponse.postValue(Resource.Loading())
        val response=repositry.resetPassword(resetRequest)
        resetPasswordesponse.postValue(response?.let { handleResetPassword(it) })
    }

    private fun handleResetPassword(response: Response<ResetResponse>): Resource<ResetResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<ResetResponse>(response.errorBody()!!.string(), ResetResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }


    fun updatePassword(resetChangePassword: ResetChangePassword)=viewModelScope.launch(Dispatchers.IO+handler) {
        updatePasswordesponse.postValue(Resource.Loading())
        val response=repositry.updatePassword(resetChangePassword)
        updatePasswordesponse.postValue(response?.let { handleUpdatePassword(it) })
    }

    private fun handleUpdatePassword(response: Response<UpdatePasswordResponse>): Resource<UpdatePasswordResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<UpdatePasswordResponse>(response.errorBody()!!.string(), UpdatePasswordResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest)=viewModelScope.launch(Dispatchers.IO+handler) {
        changePasswordesponse.postValue(Resource.Loading())
        val response=repositry.changePassword(changePasswordRequest)
        changePasswordesponse.postValue(response?.let { handleChangePassword(it) })
    }

    private fun handleChangePassword(response: Response<ChangePasswordResponse>): Resource<ChangePasswordResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.sucess(it)
            }
        }
        val error = Gson().fromJson<ChangePasswordResponse>(response.errorBody()!!.string(), ChangePasswordResponse::class.java)
        return error.message?.let { Resource.Error(it) }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("exception","osama${exception.message.toString()}")
    }

}