package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.repo.database.FirebaseDatabaseRepository
import com.mtc.finalproject_bootcamp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesFragmentViewModel @Inject constructor(private val firebaseDatabaseRepo: FirebaseDatabaseRepository): ViewModel() {

    private var _favoriteListLiveData = MutableLiveData<Resource<List<Food>>>()
    val favoriteListLiveData: LiveData<Resource<List<Food>>> get() = _favoriteListLiveData

    init {
        loadAllFavorites()
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