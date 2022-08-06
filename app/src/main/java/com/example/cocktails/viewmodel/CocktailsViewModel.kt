package com.example.cocktails.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.network.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class CocktailsViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {
    private val _mutableCocktails = MutableLiveData<List<CocktailModel>>(emptyList())
    val cocktails: LiveData<List<CocktailModel>>
        get() = _mutableCocktails
    private var numbChar: Char = 'a'
    private val _mutableCocktail = MutableLiveData<CocktailModel>()
    val cocktail: LiveData<CocktailModel>
        get() = _mutableCocktail

    fun loadCocktails() {
        viewModelScope.launch {
            try {
                val newCocktails = repository.loadCocktails(numbChar.toString())
                val updatedCocktailsList = _mutableCocktails.value?.plus(newCocktails).orEmpty()
                _mutableCocktails.value = updatedCocktailsList
                if (numbChar in 'a'..'z') {
                    numbChar++
                    delay(500)
                } else return@launch
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }

    fun setLikeByViewModel(cocktailModel: CocktailModel) {
        repository.setLike(cocktailModel)
    }

    fun loadSelectedCocktail(id: String?) {
        viewModelScope.launch {
            try {
                _mutableCocktail.value = repository.loadCocktailDetails(id)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }

    fun shareCocktail(context: Context, cocktail: CocktailModel) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, cocktail.name + "\n" + cocktail.imgPath)
            type = "text/*"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Look at this")
        context.startActivity(shareIntent)
    }
}