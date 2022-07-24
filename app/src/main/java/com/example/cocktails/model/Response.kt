package com.example.cocktails.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(

    @Json(name = "drinks")
    val drinks: List<DrinksItem?>? = null
) : Parcelable

@Parcelize
data class DrinksItem  constructor(

    @Json(name = "strInstructionsDE")
    val strInstructionsDE: String? = null,

    @Json(name = "strImageSource")
    val strImageSource: String? = null,

    //@Json(name = "strIngredient10")
    //val strIngredient10: Any? = null,

    @Json(name = "strDrink")
    val strDrink: String? = null,

   // @Json(name = "strIngredient12")
   // val strIngredient12: Any? = null,

   // @Json(name = "strIngredient11")
   // val strIngredient11: Any? = null,

   // @Json(name = "strIngredient14")
   // val strIngredient14: Any? = null,

    @Json(name = "strCategory")
    val strCategory: String? = null,

    @Json(name = "strAlcoholic")
    val strAlcoholic: String? = null,

   // @Json(name = "strIngredient13")
   // val strIngredient13: Any? = null,

   // @Json(name = "strIngredient15")
   // val strIngredient15: Any? = null,

    @Json(name = "strCreativeCommonsConfirmed")
    val strCreativeCommonsConfirmed: String? = null,

    @Json(name = "strIBA")
    val strIBA: String? = null,

    @Json(name = "strVideo")
    val strVideo: String? = null,

    @Json(name = "strTags")
    val strTags: String? = null,

    @Json(name = "strInstructions")
    val strInstructions: String? = null,

    @Json(name = "strIngredient1")
    val strIngredient1: String? = null,

    @Json(name = "strIngredient3")
    val strIngredient3: String? = null,

    @Json(name = "strIngredient2")
    val strIngredient2: String? = null,

  //  @Json(name = "strIngredient5")
   // val strIngredient5: Any? = null,

    @Json(name = "strIngredient4")
    val strIngredient4: String? = null,

   // @Json(name = "strIngredient7")
   // val strIngredient7: Any? = null,

   // @Json(name = "strIngredient6")
    //val strIngredient6: Any? = null,

  //  @Json(name = "strIngredient9")
    //val strIngredient9: Any? = null,

   // @Json(name = "strInstructionsFR")
   // val strInstructionsFR: Any? = null,

   // @Json(name = "strIngredient8")
    //val strIngredient8: Any? = null,

    @Json(name = "idDrink")
    val idDrink: String? = null,

    @Json(name = "strInstructionsES")
    val strInstructionsES: String? = null,

    @Json(name = "strInstructionsIT")
    val strInstructionsIT: String? = null,

    @Json(name = "strGlass")
    val strGlass: String? = null,

  //  @Json(name = "strMeasure12")
   // val strMeasure12: Any? = null,

   // @Json(name = "strMeasure13")
   // val strMeasure13: Any? = null,

   // @Json(name = "strMeasure10")
   // val strMeasure10: Any? = null,

   // @Json(name = "strMeasure11")
   // val strMeasure11: Any? = null,

    @Json(name = "strImageAttribution")
    val strImageAttribution: String? = null,

    @Json(name = "dateModified")
    val dateModified: String? = null,

   // @Json(name = "strDrinkAlternate")
   // val strDrinkAlternate: Any? = null,

    @Json(name = "strDrinkThumb")
    val strDrinkThumb: String? = null,

   // @Json(name = "strInstructionsZH-HANT")
   // val strInstructionsZHHANT: Any? = null,

   // @Json(name = "strMeasure9")
    //val strMeasure9: Any? = null,

   // @Json(name = "strMeasure7")
    //val strMeasure7: Any? = null,

   // @Json(name = "strMeasure8")
   // val strMeasure8: Any? = null,

   // @Json(name = "strMeasure5")
   // val strMeasure5: Any? = null,

   // @Json(name = "strMeasure6")
   // val strMeasure6: Any? = null,

    @Json(name = "strMeasure3")
    val strMeasure3: String? = null,

    @Json(name = "strMeasure4")
    val strMeasure4: String? = null,

    @Json(name = "strMeasure1")
    val strMeasure1: String? = null,

    @Json(name = "strMeasure2")
    val strMeasure2: String? = null,

   // @Json(name = "strInstructionsZH-HANS")
   // val strInstructionsZHHANS: Any? = null,

   // @Json(name = "strMeasure14")
   // val strMeasure14: Any? = null,

    //@Json(name = "strMeasure15")
    //val strMeasure15: Any? = null
) : Parcelable
