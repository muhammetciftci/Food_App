package com.mtc.finalproject_bootcamp.adapter.favorite

import com.mtc.finalproject_bootcamp.model.Food

interface IFavoriteAdapterClickListener {
    fun onClickFood(food: Food)
    fun onClickFavoriteToggleButton(food: Food, isChecked:Boolean)
}