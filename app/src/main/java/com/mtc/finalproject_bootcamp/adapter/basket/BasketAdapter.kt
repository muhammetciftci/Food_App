package com.mtc.finalproject_bootcamp.adapter.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.mtc.finalproject_bootcamp.databinding.ItemBasketLayoutBinding
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.utils.Constants.Companion.FOOD_IMAGE_URL

class BasketAdapter(var basketFoodList:List<BasketFood>, val listener: IBasketAdapterClickListener) : RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding:ItemBasketLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBasketLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        YoYo.with(Techniques.FadeInLeft).duration(500).repeat(0).playOn(holder.itemView)
        val viewBinding = holder.binding
        val basketFood = basketFoodList[position]
        val totalPrice = basketFood.basketFoodPrice * basketFood.basketFoodTotalUnit

        Glide.with(holder.itemView.context).load(FOOD_IMAGE_URL+basketFood.basketFoodImageName).into(viewBinding.itemBasketImageView)
        viewBinding.basketFood = basketFood
        viewBinding.totalAmaountTextView.text = "$totalPrice ₺"

        deleteImageViewClick(basketFood,viewBinding)
    }

    override fun getItemCount(): Int {
        return basketFoodList.size
    }

    fun checkLastItem(){
        if (basketFoodList.size == 1) {
            notifyItemRemoved(0)
            basketFoodList = emptyList()
        }
    }

    private fun deleteImageViewClick(basketFood: BasketFood, binding: ItemBasketLayoutBinding){
        binding.deleteImageView.setOnClickListener {
            listener.onClickRemoveButton(basketFood)
            YoYo.with(Techniques.Shake).duration(1250).repeat(0).playOn(binding.cardView)
        }
    }

}