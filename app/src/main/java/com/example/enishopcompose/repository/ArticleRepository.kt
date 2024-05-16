package com.example.enishopcompose.repository

import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.dao.ArticleDAO
import com.example.enishopcompose.dao.DaoType
import com.example.enishopcompose.dao.network.ArticleServiceAPI


class ArticleRepository(
    private val articleDAO: ArticleDAO,
    private val articleServiceAPI: ArticleServiceAPI
) {
    suspend fun getCategories(): List<String> {
        return articleServiceAPI.getCategories()
    }

    suspend fun getArticles(): List<Article> {
        return articleServiceAPI.getArticles()
    }

    suspend fun getArticleById(id: Long, daoType: DaoType = DaoType.NETWORK): Article? {
        return when (daoType) {
            DaoType.NETWORK -> articleServiceAPI.getArticleById(id)
            DaoType.MEMORY -> articleDAO.getById(id)
        }
    }

    suspend fun addArticle(article: Article, daoType: DaoType = DaoType.NETWORK) {
        when (daoType) {
            DaoType.NETWORK -> articleServiceAPI.addArticle(article)
            DaoType.MEMORY -> articleDAO.insert(article)
        }
    }

    suspend fun deleteArticle(article: Article) {
        articleDAO.delete(article)
    }


}