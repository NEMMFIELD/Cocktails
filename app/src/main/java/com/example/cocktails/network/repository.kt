package com.example.cocktails.network

import android.content.Context
import android.content.SharedPreferences
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.model.DrinksItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface repository {
    suspend fun loadCocktails(param: String): List<CocktailModel>
    suspend fun loadCocktailDetails(id: String?): CocktailModel
}

class RepositoryImpl @Inject constructor(private val cocktailsApi: CocktailsApi, @ApplicationContext context: Context) : repository {
    private val sharedPreferences = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE)
    override suspend fun loadCocktails(param: String): List<CocktailModel> {
        val list = cocktailsApi.getCocktailsByFirstLetter(param).drinks
        val myList = ArrayList<CocktailModel>()
        for (i in list?.indices!!) {
            myList.add(
                convertResponseToModel(
                    cocktailsApi.getCocktailsByFirstLetter(param).drinks?.get(
                        i
                    )
                )
            )
        }
        return myList
    }

    override suspend fun loadCocktailDetails(id: String?): CocktailModel =
        withContext(Dispatchers.IO)
        {
            convertResponseToModel(cocktailsApi.getCocktailById(id).drinks?.first())
        }

    private fun convertResponseToModel(response: DrinksItem?): CocktailModel =
        CocktailModel(
            id = response?.idDrink,
            name = response?.strDrink,
            imgPath = response?.strDrinkThumb.toString(),
            recipe = response?.strInstructions.toString(),
            isLiked = sharedPreferences.getBoolean("Liked",false)
        )
}