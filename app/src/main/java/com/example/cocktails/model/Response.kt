package com.example.cocktails.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Response(
    @Json(name = "drinks")
    val drinks: List<DrinksItem?>? = listOf()
) : Parcelable

@Parcelize
data class DrinksItem constructor(
    @Json(name = "strDrink")
    val strDrink: String? = null,

    @Json(name = "strInstructions")
    val strInstructions: String? = null,

    @Json(name = "idDrink")
    val idDrink: String? = null,

    @Json(name = "strDrinkThumb")
    val strDrinkThumb: String? = null,
) : Parcelable
