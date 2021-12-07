package com.example.android.eyeprescriptionconverter.data.di

import android.content.Context
import com.example.android.eyeprescriptionconverter.data.db.AppDatabase
import com.example.android.eyeprescriptionconverter.data.db.SelectionDataDao
import com.example.android.eyeprescriptionconverter.data.repository.Repository
import com.example.android.eyeprescriptionconverter.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// const val APP_DATASTORE = "app_datastore"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)


    @Singleton
    @Provides
    fun provideSelectionDataDao(database: AppDatabase): SelectionDataDao =
        database.selectionDataDao()

//    @Singleton
//    @Provides
//    fun provideAppDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
//        PreferenceDataStoreFactory.create(
//            produceFile = {
//                appContext.preferencesDataStoreFile(name = APP_DATASTORE)
//            }
//        )


//    @Singleton
//    @Provides
//    fun provideFirebaseAnalytics() : FirebaseAnalytics = Firebase.analytics

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(impl: RepositoryImpl) : Repository




}