package com.mtc.finalproject_bootcamp.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.databinding.FragmentFavoritesBinding
import com.mtc.finalproject_bootcamp.databinding.ItemFavoriteLayoutBinding
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.utils.Constants

class FavoriteAdapter(var favoriteList: List<Food>, val listener: IFavoriteAdapterClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemFavoriteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemFavoriteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        YoYo.with(Techniques.FadeIn).duration(800).repeat(0).playOn(holder.itemView)
        val viewBinding = holder.binding
        val toggleButton = viewBinding.favoriteToggleButton
        val food = favoriteList[position]

        viewBinding.food = food
        Glide.with(holder.itemView.context).load(Constants.FOOD_IMAGE_URL + food.foodImageName)
            .into(viewBinding.itemFavoriteImageView)

        cardViewClick(food,viewBinding)
        toggleButtonClick(food, toggleButton)

    }

    fun updateList(newList: List<Food>) {
        favoriteList = newList
        notifyDataSetChanged()
    }

    private fun cardViewClick(food: Food, binding: ItemFavoriteLayoutBinding) {
        binding.cardView.setOnClickListener {
            listener.onClickFood(food)
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

}