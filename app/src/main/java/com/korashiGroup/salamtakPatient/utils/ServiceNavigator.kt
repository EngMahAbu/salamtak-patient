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
                serviceId == 2  -> Nursing
                serviceId >= 1 && serviceId<=9-> Search
                serviceId == 15 || serviceId == 10 -> Labs
                serviceId == 17  -> MobileClinic
                serviceId == 16  -> MedicalConsultant
                serviceId == 18  -> MonitoringDevices
                else -> Search
            }
    }
}