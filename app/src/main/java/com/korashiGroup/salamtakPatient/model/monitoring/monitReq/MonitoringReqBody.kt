package com.korashiGroup.salamtakPatient.model.monitoring.monitReq

data class MonitoringReqBody(
    val Text: String,
    val FullName: String,
    val MobileNumber: String,
    val BuildingNumber: String,
    val StreetName: String,
    val FloorNumber: String,
    val ApartmentNumber: String,
    val CityId: Int,
    val AreaId: Int,
    val NumberOfDays: Int,
    val FromDate: String,
    val ToDate: String,
    val FromTime: String,
    val ToTime: String,
    val MonitoringDeviceId: Int
)
