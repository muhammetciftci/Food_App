package com.mtc.finalproject_bootcamp.adapter.basket

import com.mtc.finalproject_bootcamp.model.BasketFood

interface IBasketAdapterClickListener {
    fun onClickRemoveButton(basketFood: BasketFood)
}