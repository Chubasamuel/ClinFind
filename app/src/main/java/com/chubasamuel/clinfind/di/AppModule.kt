package com.chubasamuel.clinfind.di

import android.content.Context
import com.chubasamuel.clinfind.data.local.AppDao
import com.chubasamuel.clinfind.data.local.AppDatabase
import com.chubasamuel.clinfind.data.remote.ApiSource
import com.chubasamuel.clinfind.data.remote.Requests
import com.chubasamuel.clinfind.data.repository.DataRepository
import com.chubasamuel.clinfind.util.DCORPrefs
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getInstance(appContext)

    @Singleton
    @Provides
    fun provideAppDao(db: AppDatabase) = db.getAppDao()

    @Singleton
    @Provides
    fun providesDCORPrefs(@ApplicationContext appContext: Context) = DCORPrefs(appContext)

    @Singleton
    @Provides
    fun providesAppContext(@ApplicationContext appContext: Context) = appContext

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://chubasamuel.github.io/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideApiSource(requests: Requests): ApiSource = ApiSource(requests)

    @Singleton
    @Provides
    fun provideRequests(retrofit:Retrofit):Requests = retrofit.create(Requests::class.java)

    @Singleton
    @Provides
    fun provideDataRepository(dao: AppDao, api: ApiSource):DataRepository = DataRepository(dao, api)
}
