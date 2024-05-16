package com.example.enishopcompose.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.enishopcompose.dao.network.ArticleServiceAPI
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.dao.DaoType
import com.example.enishopcompose.db.AppDatabase
import com.example.enishopcompose.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _article = MutableStateFlow<Article>(Article())
    val article: StateFlow<Article>
        get() = _article

    private val _checkedFav = MutableStateFlow(false)
    val checkedFav : StateFlow<Boolean>
        get() = _checkedFav

    val isLoading = MutableStateFlow(false)

    fun initArticle(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _article.value = articleRepository.getArticleById(id)!!
            val a = articleRepository.getArticleById(id, DaoType.MEMORY)
            if(a != null){
                _checkedFav.value = true
            }
            isLoading.value = true
        }
    }

    fun updateCheckBox(){
        _checkedFav.value = !_checkedFav.value
    }

    fun addArticle(){
        viewModelScope.launch(Dispatchers.IO) {
            articleRepository.addArticle(article.value, DaoType.MEMORY)
        }
    }

    fun deleteArticle(){
        viewModelScope.launch(Dispatchers.IO) {
            articleRepository.deleteArticle(article.value)
        }
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

                return ArticleDetailViewModel(
                    ArticleRepository(
                        AppDatabase.getInstance(application.applicationContext).articleDAO(),
                        ArticleServiceAPI.ArticleApi.retrofitService
                    )
                ) as T
            }

        }
    }

}