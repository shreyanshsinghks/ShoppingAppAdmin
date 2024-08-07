package com.shreyanshsinghks.shoppingappadmin.presentation.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shreyanshsinghks.shoppingappadmin.data.repository.ShoppingAppRepoImpl
import com.shreyanshsinghks.shoppingappadmin.domain.repository.ShoppingAppRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): ShoppingAppRepo{
        return ShoppingAppRepoImpl(firestore, firebaseStorage)
    }
}