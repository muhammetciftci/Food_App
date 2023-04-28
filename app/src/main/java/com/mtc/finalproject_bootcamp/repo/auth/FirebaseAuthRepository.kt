package com.mtc.finalproject_bootcamp.repo.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mtc.finalproject_bootcamp.utils.Resource
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(val auth: FirebaseAuth) : IFirebaseAuthDao {

    override fun signUp(
        username: String,
        email: String,
        password: String,
        result: (Resource<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser!!.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(username).build()
                )
                result.invoke(Resource.Success("Register successfully"))
            }
        }
            .addOnFailureListener {
                result.invoke(Resource.Failure("login failed"))
            }
    }

    override fun signIn(
        email: String,
        password: String,
        result: (Resource<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(Resource.Success("Login successfully!"))
            }
        }.addOnFailureListener {
            result.invoke(Resource.Failure("login failed"))
        }
    }


    override fun signOut() {
        auth.signOut()
    }

}