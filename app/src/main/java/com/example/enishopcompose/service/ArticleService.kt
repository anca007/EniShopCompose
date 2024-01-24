package com.example.eni_shop.service

import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.utils.DateConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArticleService {

    companion object {
        val BASE_URL = "https://fakestoreapi.com/"

        //convertisseur intégré dans retrofit,
        // permet de transformer le JSON en objet et inversement
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateConverter)
            .build()

        //librairie qui permet de lancer nos appels API, en créant une interface
        val retrofit = Retrofit.Builder()
            //ajout du converter Json
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    //ajout des méthodes d'appel de l'api, BASE_URL + random
    @GET("products")
    suspend fun getArticles(): List<Article>

    @GET("products/{id}")
    suspend fun getArticleById(@Path("id") id : Long) : Article

    @POST("products")
    suspend fun addArticle(@Body article: Article)

    object ArticleApi {
        val retrofitService: ArticleService by lazy { retrofit.create(ArticleService::class.java) }
    }

}