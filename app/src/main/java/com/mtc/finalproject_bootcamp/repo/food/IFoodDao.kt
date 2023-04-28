package com.mtc.finalproject_bootcamp.repo.food

import com.mtc.finalproject_bootcamp.model.BasketResponse
import com.mtc.finalproject_bootcamp.model.CRUDResponse
import com.mtc.finalproject_bootcamp.model.FoodResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface IFoodDao {

    @GET("yemekler/tumYemekleriGetir.php")
    fun getAllFoods(): Call<FoodResponse>



}