package com.mtc.finalproject_bootcamp.repo.auth

import com.mtc.finalproject_bootcamp.utils.Resource

interface IFirebaseAuthDao {
    fun signUp(username:String, email: String, password: String, result: (Resource<String>) -> Unit)
    fun signIn(email: String, password: String, result: (Resource<String>) -> Unit)
    fun signOut()
}