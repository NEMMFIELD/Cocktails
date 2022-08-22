package com.example.cocktails.network

import android.content.SharedPreferences
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.model.DrinksItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface repository {
    fun loadCocktails(param: String): Flow<List<CocktailModel>>
    fun loadCocktailDetails(id: String?): Flow<CocktailModel>
    fun setLike(cocktailModel: CocktailModel)
    fun getLiked(id: String?): Boolean
}

class RepositoryImpl @Inject constructor(
    private val cocktailsApi: CocktailsApi,
    private val sharedPreferences: SharedPreferences,
) : repository {

    override fun loadCocktails(param: String): Flow<List<CocktailModel>> = flow {
        val requestApiList = cocktailsApi.getCocktailsByFirstLetter(param).drinks
        val myList: List<CocktailModel>? = requestApiList?.map { convertResponseToModel(it) }
        emit(myList ?: emptyList())
    }.flowOn(Dispatchers.IO)


    override fun loadCocktailDetails(id: String?): Flow<CocktailModel> = flow {
        emit(convertResponseToModel(cocktailsApi.getCocktailById(id).drinks?.first()))
    }.flowOn(Dispatchers.IO)

    override fun setLike(cocktailModel: CocktailModel) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(cocktailModel.id, cocktailModel.isLiked).apply()
    }

    override fun getLiked(id: String?): Boolean = sharedPreferences.getBoolean(id, false)

    private fun convertResponseToModel(response: DrinksItem?): CocktailModel =
        CocktailModel(
            id = response?.idDrink,
            name = response?.strDrink,
            imgPath = response?.strDrinkThumb.toString(),
            recipe = response?.strInstructions.toString(),
            isLiked = getLiked(response?.idDrink)
        )
}