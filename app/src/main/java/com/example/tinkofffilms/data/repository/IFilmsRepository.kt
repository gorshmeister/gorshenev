package com.example.tinkofffilms.data.repository

import com.example.tinkofffilms.data.database.FilmEntity
import com.example.tinkofffilms.data.network.model.FilmDescriptionRemote
import com.example.tinkofffilms.data.network.model.FilmPreviewRemote

interface IFilmsRepository {
    suspend fun getTopFilms(): List<FilmPreviewRemote>
    suspend fun getFilmInfo(id: Int): FilmDescriptionRemote
    suspend fun addToDb(film: FilmEntity)
    suspend fun deleteFromDb(film: FilmEntity)
    suspend fun getAllFilmsFromDb(): List<FilmEntity>
}
