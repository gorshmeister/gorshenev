package com.example.tinkofffilms.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AppDataBase.FILM)
data class FilmEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "nameRu") val nameRu: String,
    @ColumnInfo(name = "nameEn") val nameEn: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "genres") val genres: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "posterUrlPreview") val posterUrlPreview: String,
    @ColumnInfo(name = "favourite") val favourite: Boolean,
)