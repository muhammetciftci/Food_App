package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.*
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.repo.basket.BasketRepository
import com.mtc.finalproject_bootcamp.repo.database.FirebaseDatabaseRepository
import com.mtc.finalproject_bootcamp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(private val basketRepo:BasketRepository, private val firebaseDatabaseRepo: FirebaseDatabaseRepository):ViewModel() {

    private val _food = MutableLiveData<Food>()
    val food: LiveData<Food> get() = _food

    private val _foodCount = MutableLiveData<Int>()
    val foodCount: LiveData<Int> get() = _foodCount

    private val _totalFoodPrice = MutableLiveData<Int>()
    val totalFoodPrice: LiveData<Int> get() = _totalFoodPrice

    private val _addResourceLiveData = MutableLiveData<Resource<String>>()
    val addResourceLiveData: LiveData<Resource<String>> get() = _addResourceLiveData

    private var _favoriteListLiveData = MutableLiveData<Resource<List<Food>>>()
    val favoriteListLiveData: LiveData<Resource<List<Food>>> get() = _favoriteListLiveData


    init {
        _foodCount.value = 1
    }

    fun addToBasket() {
        val currentFood = food.value!!
        val basketFood = BasketFood(
            currentFood.foodId,
            currentFood.foodName,
            currentFood.foodImageName,
            currentFood.foodPrice,
            foodCount.value!!,
            null
        )
        basketRepo.addFoodToBasket(basketFood){
            _addResourceLiveData.value = it
        }
    }

    fun updateFood(food: Food){
        _food.value = food
        updateTotalPrice()
        loadAllFavorites()
    }

    fun getCurrentFood(): Food? {
        return _food.value
    }

    fun incrementCount() {
        val currentValue = _foodCount.value ?: 1
        _foodCount.value = currentValue + 1
        updateTotalPrice()
    }

    fun decrementCount() {
        val currentValue = _foodCount.value ?: 1
        val newValue = if (currentValue > 1) currentValue - 1 else 1
        _foodCount.value = newValue
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val count = foodCount.value?.toInt() ?: 0
        val price = food.value?.foodPrice?: 0
        _totalFoodPrice.value = count * price
    }


    fun loadAllFavorites(){
        firebaseDatabaseRepo.loadAllFavorites()
        _favoriteListLiveData = firebaseDatabaseRepo.getFavoriteData()
    }

    fun addToFavorite(food: Food){
        firebaseDatabaseRepo.addFavorite(food)
    }

    fun deleteFromFavorite(food: Food){
        firebaseDatabaseRepo.deleteFavorite(food)
    }



}