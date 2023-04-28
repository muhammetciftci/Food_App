package com.mtc.finalproject_bootcamp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BasketFood(
    @SerializedName("sepet_yemek_id")
    val basketFoodId: Int,
    @SerializedName("yemek_adi")
    val basketfoodName: String,
    @SerializedName("yemek_resim_adi")
    val basketFoodImageName: String,
    @SerializedName("yemek_fiyat")
    val basketFoodPrice: Int,
    @SerializedName("yemek_siparis_adet")
    var basketFoodTotalUnit: Int,
    @SerializedName("kullanici_adi")
    var userName: String?
) : Serializable