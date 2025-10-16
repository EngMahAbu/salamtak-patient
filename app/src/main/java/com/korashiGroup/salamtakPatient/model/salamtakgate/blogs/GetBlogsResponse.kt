package com.korashiGroup.salamtakPatient.model.salamtakgate.blogs


import com.google.gson.annotations.SerializedName

data class GetBlogsResponse(
    @SerializedName("Data")
    val `data`: List<DataBlogs?>?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("MessageCode")
    val messageCode: Int?,
    @SerializedName("Success")
    val success: Boolean?
)
data class DataBlogs(
    @SerializedName("CreatedDate")
    val createdDate: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("inactive")
    val inactive: Boolean?,
    @SerializedName("isVideo")
    val isVideo: Boolean?,
    @SerializedName("Links")
    val links: Any?,
    @SerializedName("Sections")
    val sections: Any?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("TypeID")
    val typeID: Int?
)