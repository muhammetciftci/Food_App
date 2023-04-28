package com.mtc.finalproject_bootcamp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Food(
    @SerializedName("yemek_id")
    val foodId: Int = 0,
    @SerializedName("yemek_adi")
    val foodName: String = "",
    @SerializedName("yemek_resim_adi")
    val foodImageName: String = "",
    @SerializedName("yemek_fiyat")
    val foodPrice: Int = 0
): Serializable