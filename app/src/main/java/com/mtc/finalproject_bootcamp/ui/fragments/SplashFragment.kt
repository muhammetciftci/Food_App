package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.databinding.FragmentSplashBinding
import com.mtc.finalproject_bootcamp.utils.Utils
import com.mtc.finalproject_bootcamp.viewmodel.SplashFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private val viewModel by viewModels<SplashFragmentViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        Utils.bottomNavInActive(requireActivity())
        splashScreenDelay()

        return binding.root
    }

    fun splashScreenDelay() {
        Handler().postDelayed({
            checkAuth()
        }, 2500)
    }

    fun checkAuth() {
        if (viewModel.checkAuth()) {
            findNavController().navigate(R.id.splashToMain)
        } else {
            findNavController().navigate(R.id.splashToSignIn)
        }
    }

}