package com.example.cocktails.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CocktailModel(
    val id: String?,
    val name: String?,
    val imgPath: String?,
    val recipe: String?
) : Parcelable
