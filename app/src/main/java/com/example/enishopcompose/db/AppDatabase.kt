package com.example.enishopcompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.enishopcompose.dao.ArticleDAO
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.utils.DateConverter
import com.example.enishopcompose.utils.DateRoomConverter

@Database(entities = [Article::class], version = 4)
@TypeConverters(DateRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    //liste de nos DAOs
    abstract fun articleDAO(): ArticleDAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            var instance = INSTANCE

            if (instance == null) {

                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "EniShop"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
            }
            return instance
        }
    }
}
