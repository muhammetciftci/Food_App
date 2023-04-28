package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.adapter.favorite.FavoriteAdapter
import com.mtc.finalproject_bootcamp.adapter.favorite.IFavoriteAdapterClickListener
import com.mtc.finalproject_bootcamp.databinding.FragmentFavoritesBinding
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils.bottomNavActive
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import com.mtc.finalproject_bootcamp.viewmodel.FavoritesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment(), IFavoriteAdapterClickListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModels<FavoritesFragmentViewModel>()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.favoritesFragment = this

        adapter = FavoriteAdapter(emptyList(), this)

        bottomNavActive(requireActivity())

        observe()

        return binding.root
    }

    fun observe() {
        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.playAnimation()
                    binding.loadAnimationView.visible()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.pauseAnimation()
                    binding.loadAnimationView.gone()
                    adapter.updateList(it.data)
                    binding.adapter = adapter
                    checkFavorites(it.data.size)
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.pauseAnimation()
                    binding.loadAnimationView.gone()
                }
            }
        })
    }

    override fun onClickFood(food: Food) {
        val directions = FavoritesFragmentDirections.favoritesToDetails(food)
        Navigation.findNavController(binding.root).navigate(directions)
    }

    override fun onClickFavoriteToggleButton(food: Food, isChecked: Boolean) {
        if (isChecked) {
            viewModel.addToFavorite(food)
        } else {
            viewModel.deleteFromFavorite(food)
        }
    }

    private fun checkFavorites(listSize: Int) {
        if (listSize > 0) {
            binding.emptyFavoritesImageView.gone()
            binding.emptyFavoritesTextView.gone()
        } else {
            binding.emptyFavoritesImageView.visible()
            binding.emptyFavoritesTextView.visible()
            YoYo.with(Techniques.FadeIn).duration(1000).repeat(0)
                .playOn(binding.emptyFavoritesImageView)
            YoYo.with(Techniques.FadeIn).duration(1000).repeat(0)
                .playOn(binding.emptyFavoritesTextView)
        }
    }

}