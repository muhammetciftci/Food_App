package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.adapter.food.FoodAdapter
import com.mtc.finalproject_bootcamp.adapter.food.IFoodAdapterItemClickListener
import com.mtc.finalproject_bootcamp.databinding.FragmentMainBinding
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.viewmodel.MainFragmentViewModel
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), IFoodAdapterItemClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: FoodAdapter
    private val viewModel by viewModels<MainFragmentViewModel>()
    private lateinit var favoriteList: List<Food>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.mainFragment = this
        binding.titleText = "Welcome, ${viewModel.getCurrentUsername()}"

        favoriteList = emptyList()
        adapter = FoodAdapter(emptyList(), favoriteList, this)

        Utils.bottomNavActive(requireActivity())

        observe()
        searchListener()

        return binding.root
    }

    private fun observe() {
        viewModel.foodLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    adapter = FoodAdapter(it.data.foodList, favoriteList, this)
                    binding.foodAdapter = adapter
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                }
            }
        })

        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, Observer {
            if (it is Resource.Success) {
                favoriteList = it.data
                adapter.updateFavoriteList(it.data)
            } else {
                favoriteList = emptyList()
            }
        })
    }

    fun searchListener() {
        binding.searchEditText.addTextChangedListener { text ->
            val currentFoodList = viewModel.getFoodList()
            val filteredList = if (text.isNullOrEmpty()) {
                currentFoodList // Arama kelimesi yoksa, tüm liste öğelerini döndür - If there is no search word, return all list items
            } else {
                currentFoodList.filter { it.foodName.contains(text, ignoreCase = true) }
            }
            adapter.updateList(filteredList)
        }
    }

    fun exitClick() {
        Snackbar.make(binding.root, "Are you sure you want to sign out?", Snackbar.LENGTH_LONG)
            .setAction("Yes") {
                viewModel.signOut()
                findNavController().navigate(R.id.mainToSignIn)
            }.show()
    }

    fun fabClick() {
        findNavController().navigate(R.id.mainToBasket)
    }


    override fun onClickFood(food: Food) {
        val directions = MainFragmentDirections.mainToDetails(food = food)
        Navigation.findNavController(binding.root).navigate(directions)
    }

    override fun onClickAddButton(food: Food) {
        val basketFood = BasketFood(
            food.foodId,
            food.foodName,
            food.foodImageName,
            food.foodPrice,
            1,
            null
        )
        var isButtonClicked = false //iki defa eklemeyi önlemek için-Precaution not to add 2 times
        Snackbar.make(binding.root, "Add ${food.foodName} to basket?", Snackbar.LENGTH_LONG)
            .setAction("YES") {
                if (!isButtonClicked) {
                    isButtonClicked = true
                    viewModel.addToBasket(basketFood)
                    Toast.makeText(requireContext(), "Added to basket", Toast.LENGTH_SHORT).show()
                }
            }.show()
    }

    override fun onClickFavoriteToggleButton(food: Food, isChecked: Boolean) {
        if (isChecked) {
            viewModel.addToFavorite(food)
        } else {
            viewModel.deleteFromFavorite(food)
        }
    }


}

