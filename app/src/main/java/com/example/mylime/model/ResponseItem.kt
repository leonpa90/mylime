package com.example.mylime.model


import com.google.gson.annotations.SerializedName

data class ResponseItem(
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("image_url")
    val imageUrl: String? = "",
    @SerializedName("name")
    val name: String? = "",
)