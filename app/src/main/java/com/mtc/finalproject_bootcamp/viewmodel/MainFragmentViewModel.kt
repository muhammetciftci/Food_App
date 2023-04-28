package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.model.FoodResponse
import com.mtc.finalproject_bootcamp.repo.basket.BasketRepository
import com.mtc.finalproject_bootcamp.repo.database.FirebaseDatabaseRepository
import com.mtc.finalproject_bootcamp.repo.food.FoodRepository

import com.mtc.finalproject_bootcamp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val foodRepo: FoodRepository,
    private val basketRepo: BasketRepository,
    private val firebaseDatabaseRepo:FirebaseDatabaseRepository
) :
    ViewModel() {

    private var _foodLiveData = MutableLiveData<Resource<FoodResponse>>()
    val foodLiveData: LiveData<Resource<FoodResponse>> get() = _foodLiveData

    val foodListLiveData = MutableLiveData<List<Food>>()

    private val _addBasketResourceLiveData = MutableLiveData<Resource<String>>()
    val addBasketResourceLiveData: LiveData<Resource<String>> get() = _addBasketResourceLiveData


    private var _favoriteListLiveData = MutableLiveData<Resource<List<Food>>>()
    val favoriteListLiveData: LiveData<Resource<List<Food>>> get() = _favoriteListLiveData


    init {
        loadAllFood()
        loadAllFavorites()
        observe()
    }



    fun addToBasket(basketFood: BasketFood) {
        basketRepo.addFoodToBasket(basketFood){
            _addBasketResourceLiveData.value = it
        }
    }

    fun addToFavorite(food: Food){
        firebaseDatabaseRepo.addFavorite(food)
    }

    fun deleteFromFavorite(food: Food){
        firebaseDatabaseRepo.deleteFavorite(food)
    }

    fun loadAllFavorites(){
        firebaseDatabaseRepo.loadAllFavorites()
        _favoriteListLiveData = firebaseDatabaseRepo.getFavoriteData()
    }

    fun loadAllFood() {
        foodRepo.getAllFoods{}
        _foodLiveData = foodRepo.getFoodList()
    }

    fun observe(){
        // arama için - for search
        foodLiveData.observeForever {
            if (it is Resource.Success){
                foodListLiveData.value = it.data.foodList
            }
        }
    }

    fun getFoodList():List<Food>{
        val list = foodListLiveData.value
        return if (list.isNullOrEmpty()){
            emptyList()
        }else{
            list
        }
    }

    fun getCurrentUsername():String{
        if (auth.currentUser != null){
            return auth.currentUser?.displayName!!
        }else{
            return ""
        }
    }

    fun signOut() {
        auth.signOut()
    }

}