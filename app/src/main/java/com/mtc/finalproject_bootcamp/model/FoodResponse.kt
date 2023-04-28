package com.mtc.finalproject_bootcamp.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("yemekler")
    val foodList: List<Food>,
    @SerializedName("success")
    val success: Int
)