package com.example.enishopcompose.bo


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.Date

@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo("name")
    @Json(name = "title")
    var name: String = "",
    @ColumnInfo("description")
    var description: String = "",
    @ColumnInfo("price")
    var price: Float = 0f,
    @ColumnInfo("date")
    var date: Date = Date(),
    @Json(name = "image")
    var urlImage: String = "",
    var category : String = "",
    @Json(name = "rate")
    var rate : Float = 0f
)




