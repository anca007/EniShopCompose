package com.example.enishopcompose.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.enishopcompose.dao.network.ArticleServiceAPI
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.db.AppDatabase
import com.example.enishopcompose.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleListViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>>
        get() = _categories

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>>
        get() = _articles

    var selectedCategory = MutableStateFlow<String?>(null)
    var isLoading = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //séquentiel
//            articleRepository.getArticles()
//            articleRepository.getCategories()
//            isLoading.value = true
            //envoie tous les appels en même temps
            val a = async { _articles.value = articleRepository.getArticles() }
            val c = async { _categories.value = articleRepository.getCategories() }
            awaitAll(a, c)
            isLoading.value = true

        }
    }

    fun setCategory(category: String) {
        var newCategory: String? = category

        if (selectedCategory.value == newCategory) {
            newCategory = null
        }
        selectedCategory.value = newCategory
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ArticleListViewModel(
                    ArticleRepository(
                        AppDatabase.getInstance(application.applicationContext).articleDAO(),
                        ArticleServiceAPI.ArticleApi.retrofitService
                    )
                ) as T
            }

        }
    }

}