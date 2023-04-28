package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.databinding.FragmentDetailsBinding
import com.mtc.finalproject_bootcamp.viewmodel.DetailsFragmentViewModel
import com.mtc.finalproject_bootcamp.utils.Constants.Companion.FOOD_IMAGE_URL
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val bundle: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailsFragmentViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.detailsFragment = this

        Utils.bottomNavInActive(requireActivity())
        getFoodBundle()
        observe()
        toggleButtonClick()

        return binding.root
    }

    fun observe() {
        viewModel.foodCount.observe(viewLifecycleOwner, Observer {
            binding.foodCount.text = it.toString()
        })

        viewModel.food.observe(viewLifecycleOwner, Observer { food ->
            binding.food = food
            Glide.with(requireContext()).load(FOOD_IMAGE_URL + food.foodImageName)
                .into(binding.detailsImageView)
        })

        viewModel.totalFoodPrice.observe(viewLifecycleOwner, Observer {
            binding.totalPrice.text = "Total: ${it.toDouble()} ₺"
        })

        viewModel.addResourceLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    Snackbar.make(binding.root, "Added to basket", Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                }
            }
        })

        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, Observer {
            if (it is Resource.Success){
                val favoriteList = it.data
                val currentFood = viewModel.getCurrentFood()
                if (favoriteList.contains(currentFood)){
                    binding.favoriteToggleButton.setBackgroundResource(R.drawable.favorite_selected_icon)
                    binding.favoriteToggleButton.isChecked = true
                } else {
                    binding.favoriteToggleButton.setBackgroundResource(R.drawable.favorite_default_icon)
                    binding.favoriteToggleButton.isChecked = false
                }
            }
        })
    }

    fun addCountButtonClick() {
        viewModel.incrementCount()
    }

    fun removeCountButtonClick() {
        viewModel.decrementCount()
    }

    fun addToBasketClick() {
        viewModel.addToBasket()
        binding.addToBasketButton.isEnabled = false
    }

    fun backIconClick() {
        findNavController().navigateUp()
    }

    fun getFoodBundle() {
        val food = bundle.food
        viewModel.updateFood(food)
    }

    private fun toggleButtonClick(){
        binding.favoriteToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            val currentFood = viewModel.getCurrentFood()
            if (isChecked) {
                if (currentFood != null){
                    binding.favoriteToggleButton.setBackgroundResource(R.drawable.favorite_selected_icon)
                    viewModel.addToFavorite(currentFood)
                }
            } else {
                if (currentFood != null){
                    binding.favoriteToggleButton.setBackgroundResource(R.drawable.favorite_default_icon)
                    viewModel.deleteFromFavorite(currentFood)
                }
            }
        }
    }

}