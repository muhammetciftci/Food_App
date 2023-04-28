package com.mtc.finalproject_bootcamp.repo.basket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.model.BasketResponse
import com.mtc.finalproject_bootcamp.model.CRUDResponse
import com.mtc.finalproject_bootcamp.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class BasketRepository @Inject constructor(
    private val retrofit: Retrofit,
    private val auth: FirebaseAuth
) {
    private var basketDao: IBasketDao
    private var userName: String
    var basketLiveData = MutableLiveData<Resource<BasketResponse>>()

    init {
        basketDao = retrofit.create(IBasketDao::class.java)
        userName = auth.currentUser?.displayName!!
        loadAllBasketFoods()
    }

    fun loadAllBasketFoods(){
        getAllBasketFoods {
            basketLiveData.value = it
        }
    }

    fun getAllBasketFoods(result: (Resource<BasketResponse>) -> Unit) {
        basketDao.getAllFoodsBasket(userName).enqueue(object : Callback<BasketResponse> {
            override fun onResponse(
                call: Call<BasketResponse>,
                response: Response<BasketResponse>
            ) {
                if (response.isSuccessful) {
                    result.invoke(Resource.Success(response.body()!!))
                }
            }
            override fun onFailure(call: Call<BasketResponse>, t: Throwable) {
                result.invoke(Resource.Failure("error"))
            }
        })
    }

    fun getBasketFoodList(): MutableLiveData<Resource<BasketResponse>> {
        return basketLiveData
    }

    // Bu metot, sepette aynı ürün var mı diye bakar, varsa günceller yoksa ekler. - This method checks the basket and adds based on the result
    fun addFoodToBasket(basketFood: BasketFood, result: (Resource<String>) -> Unit) {
        var isFoodAdded = false
        result.invoke(Resource.Loading)
        getAllBasketFoods {
            when (it) {
                is Resource.Loading -> {
                    result.invoke(Resource.Loading)
                }
                is Resource.Success -> {
                    val foods = it.data.foods
                    if (foods.isNotEmpty()) {
                        for(food in foods){
                            if (food.basketfoodName == basketFood.basketfoodName) {
                                isFoodAdded = true
                                basketFood.basketFoodTotalUnit += food.basketFoodTotalUnit
                                addFoodToBasketApi(basketFood)
                                deleteFoodBasket(food.basketFoodId){}
                                result.invoke(Resource.Success("added basket"))
                            }
                        }
                    }
                    if (!isFoodAdded) {
                        addFoodToBasketApi(basketFood)
                        result.invoke(Resource.Success("added basket"))
                    }
                }
                is Resource.Failure -> {
                    addFoodToBasketApi(basketFood)
                    result.invoke(Resource.Success("added basket"))
                }
            }

        }
    }

    fun addFoodToBasketApi(basketFood: BasketFood) {
        basketDao.addFoodToBasket(
            basketFood.basketfoodName,
            basketFood.basketFoodImageName,
            basketFood.basketFoodPrice,
            basketFood.basketFoodTotalUnit,
            userName
        ).enqueue(object : Callback<CRUDResponse> {
            override fun onResponse(call: Call<CRUDResponse>, response: Response<CRUDResponse>) {
                if (response.isSuccessful) {
                    Log.d("addFoodToBasket", "added basket")
                }
            }

            override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                Log.d("addFoodToBasket", "${t.message}")
            }
        })
    }


    fun deleteFoodBasket(basketFoodId: Int, result: (Resource<String>) -> Unit) {
        result.invoke(Resource.Loading)
        basketDao.deleteFoodBasket(basketFoodId, userName).enqueue(object : Callback<CRUDResponse> {
            override fun onResponse(call: Call<CRUDResponse>, response: Response<CRUDResponse>) {
                if (response.isSuccessful) {
                    result.invoke(Resource.Success("successful"))
                }
            }

            override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                result.invoke(Resource.Failure("failure"))
            }
        })
    }


}