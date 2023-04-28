package com.mtc.finalproject_bootcamp.repo.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mtc.finalproject_bootcamp.model.Food
import com.mtc.finalproject_bootcamp.utils.Constants.Companion.FIREBASE_FAVORITES_DB
import com.mtc.finalproject_bootcamp.utils.Resource
import javax.inject.Inject

class FirebaseDatabaseRepository @Inject constructor(val firebaseDatabaseRef:DatabaseReference,val auth:FirebaseAuth): IFirebaseDatabaseDao {

    private var userName: String
    var favoriteLiveData = MutableLiveData<Resource<List<Food>>>()

    init {
        userName = auth.currentUser?.displayName!!
        loadAllFavorites()
    }

    fun loadAllFavorites(){
        getAllFavorites {
            favoriteLiveData.value = it
        }
    }

    fun getFavoriteData(): MutableLiveData<Resource<List<Food>>> {
        return favoriteLiveData
    }

    override fun addFavorite(food: Food) {
        firebaseDatabaseRef.child(FIREBASE_FAVORITES_DB).child(userName).child(food.foodId.toString()).setValue(food).addOnSuccessListener {
           Log.d("addFavorite():", "successful")
        }.addOnFailureListener {
            Log.d("addFavorite():", "failure")
        }
    }

    override fun getAllFavorites(result: (Resource<List<Food>>) -> Unit) {
        result.invoke(Resource.Loading)
        firebaseDatabaseRef.child(FIREBASE_FAVORITES_DB).child(userName).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteFoods = arrayListOf<Food>()
                for (foodSnapshot in snapshot.children) {
                    val food = foodSnapshot.getValue(Food::class.java)
                    favoriteFoods.add(food!!)
                }
                result.invoke(Resource.Success(favoriteFoods))
            }
            override fun onCancelled(error: DatabaseError) {
                result.invoke(Resource.Failure(error.message))
            }
        })
    }

    override fun deleteFavorite(food: Food) {
        firebaseDatabaseRef.child(FIREBASE_FAVORITES_DB).child(userName).child(food.foodId.toString()).removeValue().addOnSuccessListener {
            Log.d("deleteFavorite():", "successful")
        }.addOnFailureListener {
            Log.d("deleteFavorite():", "failure")
        }
    }




}