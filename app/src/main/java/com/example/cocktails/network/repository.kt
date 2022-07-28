package com.example.cocktails.network

import com.example.cocktails.model.CocktailModel
import com.example.cocktails.model.DrinksItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface repository {
    suspend fun loadCocktails(param: String): List<CocktailModel>
    suspend fun loadCocktailDetails(id: String?): CocktailModel
}

class RepositoryImpl @Inject constructor(private val cocktailsApi: CocktailsApi) : repository {
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
            recipe = response?.strInstructions.toString()
        )
}