package com.korashiGroup.salamtakPatient.model.offers


import com.google.gson.annotations.SerializedName

data class OffersDetailsResponse(
    @SerializedName("Data")
    val `data`: List<OfferDetails>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)
data class OfferDetails(
    @SerializedName("bookings")
    val bookings: Int,
    @SerializedName("CS_Comment")
    val cSComment: String,
    @SerializedName("Desc")
    val desc: String,
    @SerializedName("DeviceName")
    val deviceName: String,
    @SerializedName("DiscountPerc")
    val discountPerc: Int,
    @SerializedName("Doctor_Comment")
    val doctorComment: String,
    @SerializedName("doctorDto")
    val doctorDto: DoctorDto,
    @SerializedName("EndDate")
    val endDate: String,
    @SerializedName("getDoctorWorkingSchedualDtos")
    val getDoctorWorkingSchedualDtos: List<GetDoctorWorkingSchedualDto>,
    @SerializedName("HealthEntityDTO")
    val healthEntityDTO: HealthEntityDTO,
    @SerializedName("HealthEntityId")
    val healthEntityId: Int,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("L1ID")
    val l1ID: Int,
    @SerializedName("L1image")
    val l1image: String,
    @SerializedName("L1name")
    val l1name: String,
    @SerializedName("L2ID")
    val l2ID: Int,
    @SerializedName("L2Name")
    val l2Name: String,
    @SerializedName("L3ID")
    val l3ID: Int,
    @SerializedName("L3name")
    val l3name: String,
    @SerializedName("Offer_Categoriesid")
    val offerCategoriesid: Int,
    @SerializedName("OfferStatus")
    val offerStatus: Int,
    @SerializedName("OfferSubCategoryID")
    val offerSubCategoryID: Int,
    @SerializedName("PriceAfter")
    val priceAfter: Int,
    @SerializedName("PriceBefore")
    val priceBefore: Int,
    @SerializedName("ServiceName")
    val serviceName: String,
    @SerializedName("SessionCount")
    val sessionCount: Int,
    @SerializedName("SpecID")
    val specID: Int,
    @SerializedName("StartDate")
    val startDate: String
)