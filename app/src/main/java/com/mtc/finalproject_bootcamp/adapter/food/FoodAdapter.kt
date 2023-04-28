package com.mtc.finalproject_bootcamp.adapter.food

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.databinding.ItemFoodLayoutBinding
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.utils.Constants.Companion.FOOD_IMAGE_URL

class FoodAdapter(
    var foodList: List<Food>,
    var favoriteList: List<Food>,
    val listener: IFoodAdapterItemClickListener
) :
    RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {

    var isFirstRun = true


    inner class MyViewHolder(var binding: ItemFoodLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemFoodLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        YoYo.with(Techniques.FadeIn).duration(800).repeat(0).playOn(holder.itemView)
        val viewBinding = holder.binding
        val food = foodList[position]
        val toggleButton = viewBinding.favoriteToggleButton

        viewBinding.food = foodList[position]

        Glide.with(holder.itemView.context).load(FOOD_IMAGE_URL + food.foodImageName)
            .into(viewBinding.foodImageView)


        cardViewClick(food, viewBinding)
        addToBasketImageViewClick(food, viewBinding)
        toggleButtonClick(food, toggleButton)
        checkToggleButton(food, toggleButton)

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun updateList(newList: List<Food>) {
        foodList = newList
        notifyDataSetChanged()
    }

    fun updateFavoriteList(newList: List<Food>) {
        favoriteList = newList
        if (isFirstRun) {
            notifyDataSetChanged()
            isFirstRun = false
        }
    }

    private fun toggleButtonClick(food: Food, toggleButton: ToggleButton) {
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                toggleButton.setBackgroundResource(R.drawable.favorite_selected_icon)
            } else {
                toggleButton.setBackgroundResource(R.drawable.favorite_default_icon)
            }
            listener.onClickFavoriteToggleButton(food, isChecked)
        }
    }

    private fun checkToggleButton(food: Food, toggleButton: ToggleButton) {
        if (favoriteList.contains(food)) {
            toggleButton.setBackgroundResource(R.drawable.favorite_selected_icon)
            toggleButton.isChecked = true
        } else {
            toggleButton.setBackgroundResource(R.drawable.favorite_default_icon)
            toggleButton.isChecked = false
        }
    }

    private fun cardViewClick(food: Food, binding: ItemFoodLayoutBinding) {
        binding.cardView.setOnClickListener {
            listener.onClickFood(food)
        }
    }

    private fun addToBasketImageViewClick(food: Food, binding: ItemFoodLayoutBinding) {
        binding.addImageView.setOnClickListener {
            YoYo.with(Techniques.Bounce).duration(1000).repeat(0).playOn(binding.cardView)
            listener.onClickAddButton(food)
        }
    }


}