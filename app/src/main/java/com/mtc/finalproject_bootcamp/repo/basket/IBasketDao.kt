package com.mtc.finalproject_bootcamp.repo.basket

import com.mtc.finalproject_bootcamp.model.BasketResponse
import com.mtc.finalproject_bootcamp.model.CRUDResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IBasketDao {

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    fun addFoodToBasket(
        @Field("yemek_adi") foodName: String,
        @Field("yemek_resim_adi") foodImageName: String,
        @Field("yemek_fiyat") foodPrice: Int,
        @Field("yemek_siparis_adet") foodTotalOrder: Int,
        @Field("kullanici_adi") userName: String
    ): Call<CRUDResponse>

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    fun getAllFoodsBasket(@Field("kullanici_adi") userName: String): Call<BasketResponse>

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    fun deleteFoodBasket(
        @Field("sepet_yemek_id") basketFoodId: Int,
        @Field("kullanici_adi") userName: String
    ): Call<CRUDResponse>

}