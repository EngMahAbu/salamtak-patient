package com.korashiGroup.salamtakPatient.model.deleteuser


import com.google.gson.annotations.SerializedName

data class DeleteUserResponse(
    @SerializedName("Data")
    val data: DeleteData,
    @SerializedName("Message")
    val message: String,
    @SerializedName("MessageCode")
    val messageCode: Int,
    @SerializedName("Success")
    val success: Boolean
)
data class DeleteData(
    @SerializedName("Statues")
    val statues: Boolean
)