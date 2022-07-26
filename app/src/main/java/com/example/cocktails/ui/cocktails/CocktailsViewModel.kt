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
    private var firstChar: Char = 'a'

    init {
        viewModelScope.launch {
            loadCocktails()
        }
    }

    fun loadCocktails() {
        viewModelScope.launch {
            try {
                repository.loadCocktails(firstChar.toString())
                    .collect {
                        cocktails += it
                        _postStateFlow.value = ApiState.Success(cocktails)
                        if (firstChar in 'a'..'z') {
                            firstChar++
                            delay(PAUSE_AFTER_SWITCHING_LETTER)
                        } else return@collect
                    }
            } catch (e: Exception) {
                _postStateFlow.value = ApiState.Failure(e)
            }
        }
    }

    fun searchInList(text: String) {
        val filteredlist: ArrayList<CocktailModel> = ArrayList()

        filteredlist += cocktails.filter {
            it.name?.lowercase()?.contains(text.lowercase()) == true
        }

        if (filteredlist.isEmpty()) {
            Log.d("NoData", "No Data Found")
        } else {
            _postStateFlow.value = ApiState.Success(filteredlist)
        }
    }

    fun setLikeByViewModel(cocktailModel: CocktailModel) {
        repository.setLike(cocktailModel)
    }
}