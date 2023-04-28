package com.mtc.finalproject_bootcamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mtc.finalproject_bootcamp.repo.auth.IFirebaseAuthDao
import com.mtc.finalproject_bootcamp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthFragmentsViewModel @Inject constructor(
    private val authRepository: IFirebaseAuthDao,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signUp = MutableLiveData<Resource<String>>()
    val signUp: LiveData<Resource<String>> get() = _signUp

    private val _signIn = MutableLiveData<Resource<String>>()
    val signIn: LiveData<Resource<String>> get() = _signIn

    fun signUp(username: String, email: String, password: String) {
        _signUp.value = Resource.Loading
        authRepository.signUp(username, email, password) {
            _signUp.value = it
        }
    }

    fun signIn(email: String, password: String) {
        _signIn.value = Resource.Loading
        authRepository.signIn(email, password) {
            _signIn.value = it
        }
    }

    fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }

}