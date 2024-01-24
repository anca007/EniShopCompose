package com.example.enishopcompose.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.enishopcompose.bo.Article

@Dao
interface ArticleDAO {

    @Insert
    fun insert(article: Article): Long

    @Query("SELECT * FROM Article WHERE id = :id")
    fun getById(id: Long): Article?

    @Delete
    fun delete(article: Article)

    @Query("SELECT * FROM Article")
    fun getAll(): List<Article>

}