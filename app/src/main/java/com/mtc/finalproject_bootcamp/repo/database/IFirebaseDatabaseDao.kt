package com.mtc.finalproject_bootcamp.repo.database

import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.utils.Resource

interface IFirebaseDatabaseDao {
    fun addFavorite(food: Food)
    fun getAllFavorites(result: (Resource<List<Food>>) -> Unit)
    fun deleteFavorite(food: Food)
}