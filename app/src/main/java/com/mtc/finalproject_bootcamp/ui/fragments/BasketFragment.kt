package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import com.mtc.finalproject_bootcamp.adapter.basket.BasketAdapter
import com.mtc.finalproject_bootcamp.adapter.basket.IBasketAdapterClickListener
import com.mtc.finalproject_bootcamp.databinding.FragmentBasketBinding
import com.mtc.finalproject_bootcamp.model.BasketFood
import com.mtc.finalproject_bootcamp.viewmodel.BasketFragmentViewModel
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils.bottomNavInActive
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BasketFragment : Fragment(), IBasketAdapterClickListener {

    private lateinit var binding: FragmentBasketBinding
    private lateinit var adapter: BasketAdapter
    private val viewModel by viewModels<BasketFragmentViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        binding.basketFragment = this

        bottomNavInActive(requireActivity())

        observe()

        return binding.root
    }

    fun backIconClick() {
        findNavController().navigateUp()
    }

    fun observe() {
        viewModel.basketFoodLiveData.observe(viewLifecycleOwner, Observer {
            var listSize = 0
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    val basketFoodList = it.data.foods
                    adapter = BasketAdapter(basketFoodList, this)
                    binding.basketAdapter = adapter
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    listSize = basketFoodList.size
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                }
            }
            checkBasket(listSize)
        })

        viewModel.deleteLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    adapter.checkLastItem()
                    viewModel.loadFoodsBasket()
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                }
            }
        })

        viewModel.totalPriceLiveData.observe(viewLifecycleOwner, Observer {
            binding.totalPrice.text = "$it ₺"
        })
    }


    private fun checkBasket(listSize: Int) {
        if (listSize > 0) {
            binding.emptyBasketImageView.gone()
            binding.emptyBasketTextView.gone()
        } else {
            binding.emptyBasketImageView.visible()
            binding.emptyBasketTextView.visible()
            YoYo.with(Techniques.FadeIn).duration(1000).repeat(0)
                .playOn(binding.emptyBasketImageView)
            YoYo.with(Techniques.FadeIn).duration(1000).repeat(0)
                .playOn(binding.emptyBasketTextView)
        }
    }

    override fun onClickRemoveButton(basketFood: BasketFood) {
        var isButtonClicked = false //iki defa silmeyi önlemek için-Precaution not to remove 2 times
        Snackbar.make(
            binding.root, "Remove ${basketFood.basketfoodName} to basket?", Snackbar.LENGTH_LONG
        ).setAction("YES") {
                if (!isButtonClicked) {
                    isButtonClicked = true
                    viewModel.deleteFoodToBasket(basketFood.basketFoodId)
                }
            }.show()
    }

}