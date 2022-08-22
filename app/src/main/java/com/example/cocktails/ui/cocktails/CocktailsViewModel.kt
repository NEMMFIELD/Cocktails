package com.example.cocktails.ui.cocktails

import android.util.Log
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
    companion object {
        const val PAUSE_AFTER_SWITCHING_LETTER = 500L //пауза после перехода на следующий символ
    }

    var queryTextChangedJob: Job? = null
    val cocktails: MutableList<CocktailModel> = mutableListOf()
    private val _postStateFlow: MutableStateFlow<ApiState<List<CocktailModel>>> =
        MutableStateFlow(ApiState.Empty)
    val postStateFlow: StateFlow<ApiState<List<CocktailModel>>>
        get() = _postStateFlow
    private var numbChar: Char = 'a'

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
                            delay(PAUSE_AFTER_SWITCHING_LETTER)
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
            val newList = set.toMutableList()
            _postStateFlow.value = ApiState.Success(newList)
        }
    }

    fun setLikeByViewModel(cocktailModel: CocktailModel) {
        repository.setLike(cocktailModel)
    }
}