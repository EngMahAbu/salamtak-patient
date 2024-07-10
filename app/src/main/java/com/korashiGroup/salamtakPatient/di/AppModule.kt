package com.korashiGroup.salamtakPatient.di
import android.util.Log
import com.korashiGroup.salamtakPatient.BuildConfig
import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun getInterceptor(): Interceptor {

        return Interceptor {
            val request = it.request().newBuilder()
            val requests: Request = it.request()
            val response = it.proceed(requests)
            when (response.code) {
                400 -> {
                    //Show Bad Request Error Message
                    Log.e("tag","bad request")
                }
                401 -> {
                    //Show UnauthorizedError Message

                }
                403 -> {
                    //Show Forbidden Message
                }
                404 -> {
                    //Show NotFound Message
                    Log.e("tag","404")
                }
                // ... and so on
            }


            request.addHeader("Authorization", "Bearer ")
            val actualRequest = request.build()
            it.proceed(it.request())
        }
    }

    @Provides
    @Singleton
    fun getOkHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {


        val logging=HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

//        val levelType: HttpLoggingInterceptor.Level = if (BuildConfig.DEBUG)
//            HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//        val logging = HttpLoggingInterceptor().setLevel(levelType)
        val httpBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

        return httpBuilder
            .addInterceptor(logging)
            .build()


    }


    @Provides
    fun okHttpClient(): OkHttpClient {

        val levelType: HttpLoggingInterceptor.Level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }



        private val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    private fun getOkHttpClient(): OkHttpClient {
    //    val token= LoginFragment.instance?.sp?.getUserToken(TOKEN)

        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {

            okHttpClientBuilder
                .addInterceptor(logging)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)

        }

        okHttpClientBuilder.addInterceptor { chain ->

            try {
                val originalRequest = chain.request()
                val requestBuilder =
                    originalRequest.newBuilder()

                requestBuilder.addHeader("Authorization", "Bearer ${DateUtils.getToken()}")

                chain.proceed(requestBuilder.build())
            } catch (exception: SocketTimeoutException) {
                exception.printStackTrace()
                chain.proceed(chain.request())
            }

        }

        return okHttpClientBuilder.build()
    }


    @Provides
    @Singleton
    fun provideRetrofitInstance(
        // Potential dependencies of this type
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

}