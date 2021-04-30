package com.machineinsight_it.smartgarden.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.machineinsight_it.smartgarden.BuildConfig
import com.machineinsight_it.smartgarden.data.firebase.FirebaseDataStore
import com.machineinsight_it.smartgarden.data.repository.NodeDataRepository
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DataModule.BindingsModule::class])
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun provideFirebaseDatabase(): FirebaseDataStore =
        FirebaseDataStore(
            BuildConfig.FIREBASE_EMAIL, BuildConfig.FIREBASE_PASSWORD, FirebaseAuth.getInstance(),
            FirebaseDatabase.getInstance().reference
        )

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindingsModule {
        @Binds
        abstract fun bindNodeRepository(nodeDataRepository: NodeDataRepository): NodeRepository
    }
}