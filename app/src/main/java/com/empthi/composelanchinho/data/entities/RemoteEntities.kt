package com.empthi.composelanchinho.data.entities

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("meals") val data: T?
)

data class Food(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val imageUrl: String,
    @SerializedName("strTags") val tags: String?
)