package com.example.cocktails.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.network.ApiState
import com.example.cocktails.network.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CocktailsViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {
    var queryTextChangedJob: Job? = null
    val cocktails: MutableList<CocktailModel> = mutableListOf()
    private val _postStateFlow = MutableStateFlow<ApiState>(ApiState.Empty)
    val postStateFlow: StateFlow<ApiState>
        get() = _postStateFlow
    private var numbChar: Char = 'a'
    private val _mutableCocktailDetails = MutableLiveData<CocktailModel>()
    val cocktailDetails: LiveData<CocktailModel>
        get() = _mutableCocktailDetails

    init {
        viewModelScope.launch {
            loadCocktails()
        }
    }

    fun loadCocktails() {
        viewModelScope.launch {
            try {
                repository.loadCocktails(numbChar.toString())
                    .collect {
                        cocktails += it
                        _postStateFlow.value = ApiState.Success(cocktails)
                        if (numbChar in 'a'..'z') {
                            numbChar++
                            delay(500)
                        } else return@collect
                }
            } catch (e: Exception) {
                _postStateFlow.value = ApiState.Failure(e)
            }
        }
    }

    fun searchInList(text: String, list: List<CocktailModel>) {
        val filteredlist: ArrayList<CocktailModel> = ArrayList()
        for (item in list) {
            if (item.name?.lowercase()?.contains(text.lowercase()) == true) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Log.d("NoData", "No Data Found")
        } else {
            val set = filteredlist.toSet()
            val newList = set.toList()
            _postStateFlow.value = ApiState.Success(newList)
        }
    }

    fun setLikeByViewModel(cocktailModel: CocktailModel) {
        repository.setLike(cocktailModel)
    }

    fun loadSelectedCocktail(id: String?) {
        viewModelScope.launch {
            try {
                _mutableCocktailDetails.value = repository.loadCocktailDetails(id)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }

    fun shareCocktail(context: Context, cocktail: CocktailModel) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, cocktail.name + "\n" + cocktail.imgPath)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Look at this")
        context.startActivity(shareIntent)
    }
}