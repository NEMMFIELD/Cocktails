package com.example.cocktails.network


import com.example.cocktails.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApi {

    @GET("1/search.php?")
    suspend fun getCocktailsByFirstLetter(@Query("f") firstLetter: String): Response

    @GET("1/lookup.php?")
    suspend fun getCocktailById(@Query("i") id: String?): Response
}