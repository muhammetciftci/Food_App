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
import com.mtc.finalproject_bootcamp.databinding.FragmentSignUpBinding
import com.mtc.finalproject_bootcamp.viewmodel.AuthFragmentsViewModel
import com.mtc.finalproject_bootcamp.utils.Constants.Companion.turkishCharsRegex
import com.mtc.finalproject_bootcamp.utils.Resource
import com.mtc.finalproject_bootcamp.utils.Utils
import com.mtc.finalproject_bootcamp.utils.Utils.areNullOrEmpty
import com.mtc.finalproject_bootcamp.utils.Utils.isPasswordValid
import com.mtc.finalproject_bootcamp.utils.Utils.isValidEmail
import com.mtc.finalproject_bootcamp.utils.gone
import com.mtc.finalproject_bootcamp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<AuthFragmentsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.signUpFragment = this

        Utils.bottomNavInActive(requireActivity())

        observe()


        return binding.root
    }


    private fun observe() {
        viewModel.signUp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.loadAnimationView.visible()
                    binding.loadAnimationView.playAnimation()
                }
                is Resource.Success -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    findNavController().navigate(R.id.signUpToSignIn)
                    Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT)
                        .show()
                }
                is Resource.Failure -> {
                    binding.loadAnimationView.gone()
                    binding.loadAnimationView.pauseAnimation()
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun goToSignInTextClick() {
        findNavController().navigate(R.id.signUpToSignIn)
    }

    fun signUpButtonClick() {
        val username = binding.usernameSignUpEditText.text.toString()
        val email = binding.emailSignUpEditText.text.toString()
        val password = binding.passwordSignUpEditText.text.toString()

        if (areNullOrEmpty(username, email, password)) {
            if (isValidEmail(email) && isPasswordValid(password)) {
                if (!turkishCharsRegex.containsMatchIn(username)) {
                    viewModel.signUp(username, email, password)
                } else {
                    binding.usernameSignUpEditText.error =
                        "You can use only English characters in the username."
                }
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