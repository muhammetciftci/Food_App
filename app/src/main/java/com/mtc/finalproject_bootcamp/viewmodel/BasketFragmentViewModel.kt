package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mtc.finalproject_bootcamp.model.BasketResponse
import com.mtc.finalproject_bootcamp.repo.basket.BasketRepository
import com.mtc.finalproject_bootcamp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BasketFragmentViewModel @Inject constructor(
    private val basketRepo: BasketRepository
) : ViewModel() {

    private var _basketFoodLiveData = MutableLiveData<Resource<BasketResponse>>()
    val basketFoodLiveData: LiveData<Resource<BasketResponse>> get() = _basketFoodLiveData

    private var _deleteLiveData = MutableLiveData<Resource<String>>()
    val deleteLiveData: LiveData<Resource<String>> get() = _deleteLiveData

    private var _totalPriceLiveData = MutableLiveData<Double>()
    val totalPriceLiveData: LiveData<Double> get() = _totalPriceLiveData

    init {
        loadFoodsBasket()
        totalPriceObserve()
    }

    fun loadFoodsBasket() {
        basketRepo.loadAllBasketFoods()
        _basketFoodLiveData = basketRepo.getBasketFoodList()
    }

    fun deleteFoodToBasket(basketFoodId: Int) {
        basketRepo.deleteFoodBasket(basketFoodId){
            _deleteLiveData.value = it
        }
    }

    fun totalPriceObserve(){
        basketFoodLiveData.observeForever {
            var totalPrice:Double = 0.0
            if (it is Resource.Success){
                val basketFoodList = it.data.foods
                for(basketFood in basketFoodList){
                    totalPrice += basketFood.basketFoodPrice * basketFood.basketFoodTotalUnit
                }
                _totalPriceLiveData.value = totalPrice
            }else{
                _totalPriceLiveData.value = totalPrice
            }
        }
    }
}