package com.example.cocktails.di

import com.example.cocktails.network.CocktailsApi
import com.example.cocktails.network.RepositoryImpl
import com.example.cocktails.network.repository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): CocktailsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CocktailsApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(cocktailsApi: CocktailsApi): repository = RepositoryImpl(cocktailsApi)

}