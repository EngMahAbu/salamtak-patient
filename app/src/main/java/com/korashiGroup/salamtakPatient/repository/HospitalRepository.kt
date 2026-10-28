package com.korashiGroup.salamtakPatient.repository

import com.korashiGroup.salamtakPatient.api.ApiService
import com.korashiGroup.salamtakPatient.model.hospital.HospitalDoctorResponse
import com.korashiGroup.salamtakPatient.model.hospital.HospitalResponse
import com.korashiGroup.salamtakPatient.utils.DateUtils
import retrofit2.Response
import javax.inject.Inject

class HospitalRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllHospitals(
        cityId: Int?,
        areaId: Int?,
        specialistId: Int?,
        searchByName: String?
    ): Response<HospitalResponse>? {
        val culture = DateUtils.getLanguage() ?: "en"
        return apiService.getAllHospitals(
            culture = culture,
            cityId = cityId,
            areaId = areaId,
            specialistId = specialistId,
            searchByName = if (searchByName.isNullOrBlank()) null else searchByName
        )
    }

    suspend fun getHospitalDoctors(
        hospitalId: Int?,
        specialistId: Int?,
        searchByDoctorName: String?
    ): Response<HospitalDoctorResponse>? {
        val culture = DateUtils.getLanguage() ?: "en"
        return apiService.getHospitalDoctors(
            culture = culture,
            hospitalId = hospitalId,
            specialistId = specialistId,
            searchByDoctorName = if (searchByDoctorName.isNullOrBlank()) null else searchByDoctorName
        )
    }
}
