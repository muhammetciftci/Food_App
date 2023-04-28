package com.mtc.finalproject_bootcamp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mtc.finalproject_bootcamp.R
import com.mtc.finalproject_bootcamp.databinding.FragmentSignInBinding
import com.mtc.finalproject_bootcamp.viewmodel.AuthFragmentsViewModel
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<AuthFragmentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.signInFragment = this

        Utils.bottomNavInActive(requireActivity())

        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.signIn.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    findNavController().navigate(R.id.signInToMain)
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun goToSignUpTextClick() {
        findNavController().navigate(R.id.signInToSignUp)
    }

    fun signInButtonClick() {
        val email = binding.emailSignInEditText.text.toString()
        val password = binding.passwordSignInEditText.text.toString()
        if (Utils.areNullOrEmpty(email, password)) {
            if (Utils.isValidEmail(email) && Utils.isPasswordValid(password)) {
                viewModel.signIn(email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "invalid email or password(6+ chars needed)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in the blanks!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}