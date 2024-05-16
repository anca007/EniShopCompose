package com.example.enishopcompose.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.enishopcompose.dao.network.ArticleServiceAPI
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.db.AppDatabase
import com.example.enishopcompose.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "ArticleFormViewModel"

class ArticleFormViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String>
        get() = _name

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _price = MutableStateFlow(0f)
    val price = _price.asStateFlow()
    private val _date = MutableStateFlow(Date())
    val date = _date.asStateFlow()
    private val _category = MutableStateFlow("")
    val category = _category.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>>
        get() = _categories

    fun setName(name: String) {
        _name.value = name
    }

    fun setDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun setPrice(newPrice: Float) {
        _price.value = newPrice
    }

    fun setCategory(newCategory: String) {
        _category.value = newCategory
    }

    fun setDate(newDate: Date) {
        _date.value = newDate
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _categories.value = articleRepository.getCategories()
        }
    }

    fun addArticle() {
        val newArticle = Article(
            name = name.value,
            description = description.value,
            price = price.value,
            date = date.value,
            category = category.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            articleRepository.addArticle(newArticle)
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

                return ArticleFormViewModel(
                    ArticleRepository(
                        AppDatabase.getInstance(application.applicationContext).articleDAO(),
                        ArticleServiceAPI.ArticleApi.retrofitService
                    )
                ) as T
            }

        }
    }


}