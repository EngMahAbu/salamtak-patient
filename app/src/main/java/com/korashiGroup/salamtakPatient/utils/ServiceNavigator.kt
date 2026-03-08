package com.korashiGroup.salamtakPatient.utils

import com.korashiGroup.salamtakPatient.R

sealed class ServiceNavigator(val destination: Int) {

    object Search : ServiceNavigator(R.id.searchFragment)
    object Labs : ServiceNavigator(R.id.specialistLabsAndRdiologyFragment)
    object Nursing : ServiceNavigator(R.id.askingFoNurssingFragment)
    object MobileClinic : ServiceNavigator(R.id.mobileClinicFragment)
    object MedicalConsultant : ServiceNavigator(R.id.medicalConsultantFragment)
    object MonitoringDevices : ServiceNavigator(R.id.monitoringDevicesFragment)

    companion object Factory{

        fun create(serviceId: Int): ServiceNavigator =
            when {
                serviceId > 9 -> Search
                serviceId == 8 || serviceId == 9 -> Labs
                serviceId == 3  -> MobileClinic
                serviceId == 2  -> MedicalConsultant
                serviceId == 4  -> MonitoringDevices
                else -> Nursing
            }
    }
}