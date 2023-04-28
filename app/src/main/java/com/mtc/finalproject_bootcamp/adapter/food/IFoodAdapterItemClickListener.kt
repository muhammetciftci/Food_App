package com.mtc.finalproject_bootcamp.adapter.food

import com.mtc.finalproject_bootcamp.model.Food
import java.util.zip.CheckedInputStream

interface IFoodAdapterItemClickListener {
    fun onClickFood(food:Food)
    fun onClickAddButton(food: Food)
    fun onClickFavoriteToggleButton(food: Food,isChecked:Boolean)
}