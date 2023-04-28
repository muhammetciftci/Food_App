package com.mtc.finalproject_bootcamp.model

import com.google.gson.annotations.SerializedName

data class BasketResponse(
    val success: Int,
    @SerializedName("sepet_yemekler")
    val foods: List<BasketFood>
)