package com.machineinsight_it.smartgarden.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.machineinsight_it.smartgarden.BuildConfig
import com.machineinsight_it.smartgarden.data.firebase.FirebaseDataStore
import com.machineinsight_it.smartgarden.data.repository.NodeDataRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDataStore =
        FirebaseDataStore(
            BuildConfig.FIREBASE_EMAIL, BuildConfig.FIREBASE_PASSWORD, FirebaseAuth.getInstance(),
            FirebaseDatabase.getInstance().reference
        )

    @Provides
    @Singleton
    fun provideScheduleRepository(firebaseDataStore: FirebaseDataStore): NodeDataRepository =
        NodeDataRepository(firebaseDataStore)
}