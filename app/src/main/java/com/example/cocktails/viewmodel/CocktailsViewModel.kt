package com.example.cocktails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class CocktailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _mutableCocktails = MutableLiveData<List<CocktailModel>>(emptyList())
    val cocktails: LiveData<List<CocktailModel>>
        get() = _mutableCocktails

    private val _mutableCocktail = MutableLiveData<CocktailModel>()
    val cocktail: LiveData<CocktailModel>
        get() = _mutableCocktail


    fun loadFirst(param: Char) {
        viewModelScope.launch {
            try {
                val newCocktails = repository.loadCocktails(param.toString())
                val updatedCocktailsList = _mutableCocktails.value?.plus(newCocktails).orEmpty()
                _mutableCocktails.value = updatedCocktailsList
            }
            catch (e: Exception) {
            }
        }
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
}