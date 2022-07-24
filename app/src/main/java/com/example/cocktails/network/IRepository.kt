package com.example.cocktails.network

import com.example.cocktails.model.CocktailModel
import com.example.cocktails.model.DrinksItem
import com.example.cocktails.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IRepository {
    suspend fun loadCocktails(param: String): List<CocktailModel>
    suspend fun loadCocktailDetails(id:String?):CocktailModel
}

class Repository @Inject constructor(private val cocktailsApi: CocktailsApi) : IRepository {
    override suspend fun loadCocktails(param: String): List<CocktailModel>
    {
        val list = cocktailsApi.getCocktailsByFirstLetter(param).drinks
        val myList = ArrayList<CocktailModel>()
        for (i in list?.indices!!)
        {
           myList.add(convertResponseToModel(cocktailsApi.getCocktailsByFirstLetter(param).drinks?.get(i)))
        }
        return myList
    }

    override suspend fun loadCocktailDetails(id: String?):CocktailModel=
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