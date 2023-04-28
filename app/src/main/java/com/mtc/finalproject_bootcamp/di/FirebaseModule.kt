package com.mtc.finalproject_bootcamp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mtc.finalproject_bootcamp.repo.auth.FirebaseAuthRepository
import com.mtc.finalproject_bootcamp.repo.auth.IFirebaseAuthDao
import com.mtc.finalproject_bootcamp.repo.database.FirebaseDatabaseRepository
import com.mtc.finalproject_bootcamp.repo.database.IFirebaseDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): IFirebaseAuthDao {
        return FirebaseAuthRepository(auth)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseReferenceInstance(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseRepository(firebaseDatabaseRef:DatabaseReference, auth: FirebaseAuth):IFirebaseDatabaseDao{
        return FirebaseDatabaseRepository(firebaseDatabaseRef,auth)
    }


}