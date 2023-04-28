package com.mtc.finalproject_bootcamp.repo.food

import androidx.lifecycle.MutableLiveData
import com.mtc.finalproject_bootcamp.model.BasketResponse
import com.mtc.finalproject_bootcamp.model.FoodResponse
import com.mtc.finalproject_bootcamp.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class FoodRepository @Inject constructor(private val retrofit: Retrofit) {

    private var foodDao:IFoodDao
    private var foodLiveData: MutableLiveData<Resource<FoodResponse>>

    init {
        foodLiveData = MutableLiveData()
        foodDao = retrofit.create(IFoodDao::class.java)
    }

    fun getAllFoods(result: (Resource<FoodResponse>) -> Unit) {
        result.invoke(Resource.Loading)
        foodDao.getAllFoods().enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    result.invoke(Resource.Success(response.body()!!))
                }
            }
            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                result.invoke(Resource.Failure("failure"))
            }
        })
    }

    fun getFoodList():MutableLiveData<Resource<FoodResponse>>{
        getAllFoods {
            foodLiveData.value = it
        }
        return foodLiveData
    }



}