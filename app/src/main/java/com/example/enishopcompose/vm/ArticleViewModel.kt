package com.example.enishopcompose.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.eni_shop.service.ArticleService
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.dao.ArticleDAO
import com.example.enishopcompose.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class ArticleViewModel(
    private val articleDAO: ArticleDAO
) : ViewModel() {

    var article = mutableStateOf(Article())
    var articles = mutableStateOf<List<Article>>(emptyList())
    var isLoading = mutableStateOf(false)
    var checkedFav = mutableStateOf(false)

    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var price by mutableStateOf(0f)
    var date by mutableStateOf(Date())

    fun getArticleById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            article.value = ArticleService.ArticleApi.retrofitService.getArticleById(id)
            isLoading.value = true
        }
    }

    fun getAllArticles() {
        //Ajout du dispatcher.io pour lancer des appels aync qui ne bloque pas l'ui
        //permet au loading screen de fonctionner propremement
        viewModelScope.launch(Dispatchers.IO) {
            articles.value = ArticleService.ArticleApi.retrofitService.getArticles()
            isLoading.value = true
        }
    }

    fun insertArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            val newArticle = Article(
                name = name,
                description = description,
                price = price,
                date = date
            )
            ArticleService.ArticleApi.retrofitService.addArticle(newArticle)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    fun insertArticleFav() {
        viewModelScope.launch(Dispatchers.IO) {
            val newArticleFav by article
            articleDAO.insert(newArticleFav)
        }
    }

    fun deleteArticleFav() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentArticle by article
            articleDAO.delete(currentArticle)
        }
    }

    fun getArticleFavById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val articleFav = articleDAO.getById(id)
            if (articleFav != null) {
                checkedFav.value = true
            }
        }
    }

    fun getAllArticlesFav() {
        viewModelScope.launch(Dispatchers.IO) {
            articles.value = articleDAO.getAll()
            isLoading.value = true
        }
    }


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])

                return ArticleViewModel(
                    AppDatabase.getInstance(application.applicationContext).articleDAO()
                ) as T
            }

        }
    }
}

