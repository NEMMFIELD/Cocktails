package com.example.cocktails.network

import com.example.cocktails.Utils.Constants
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApi {

    @GET("1/search.php?")
    suspend fun getCocktailsByFirstLetter(@Query("f") f:String):Response

    @GET("1/lookup.php?")
    suspend fun getCocktailById(@Query("i") i: String?):Response
}