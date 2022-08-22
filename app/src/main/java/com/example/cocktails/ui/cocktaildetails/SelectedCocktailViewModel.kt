package com.example.cocktails.ui.cocktaildetails

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.network.ApiState
import com.example.cocktails.network.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedCocktailViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    state: SavedStateHandle,
) : ViewModel() {
    companion object {
        const val ID = "id"
    }

    private val _mutableCocktailDetails = MutableStateFlow<ApiState<CocktailModel>>(ApiState.Empty)
    val cocktailDetails: StateFlow<ApiState<CocktailModel>>
        get() = _mutableCocktailDetails

    init {
        val id = state.get<String>(ID)
        loadSelectedCocktail(id)
    }

    private fun loadSelectedCocktail(id: String?) {
        viewModelScope.launch {
            try {
                repository.loadCocktailDetails(id)
                    .collectLatest {
                        _mutableCocktailDetails.value = ApiState.Success(it)
                    }

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