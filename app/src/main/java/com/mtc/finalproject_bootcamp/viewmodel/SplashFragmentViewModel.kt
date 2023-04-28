package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(private val auth: FirebaseAuth):ViewModel() {

    fun checkAuth():Boolean{
        return auth.currentUser != null
    }
}