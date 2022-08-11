package com.example.cocktails.network

import com.example.cocktails.model.CocktailModel

sealed class ApiState
{
    object Loading:ApiState()
    class Failure(val message:Throwable):ApiState()
    class Success(val data:List<CocktailModel>):ApiState()
    object Empty:ApiState()
}
