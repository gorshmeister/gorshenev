package com.example.tinkofffilms.data.repository

import com.example.tinkofffilms.data.database.AppDataBase
import com.example.tinkofffilms.data.database.FilmEntity
import com.example.tinkofffilms.data.network.FilmsApi
import com.example.tinkofffilms.data.network.model.FilmDescriptionRemote
import com.example.tinkofffilms.data.network.model.FilmPreviewRemote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FilmsRepository(
    private val db: AppDataBase,
    private val api: FilmsApi,
    private val dispatcher: CoroutineDispatcher
) : IFilmsRepository {

    override suspend fun getTopFilms(): List<FilmPreviewRemote> = withContext(dispatcher) {
        api.getTopFilms().films
    }

    override suspend fun getFilmInfo(id: Int): FilmDescriptionRemote = withContext(dispatcher) {
        api.getFilmInfo(id)
    }

    override suspend fun addToDb(film: FilmEntity) = withContext(dispatcher) {
        db.filmDao().insertFilm(film)
    }

    override suspend fun deleteFromDb(film: FilmEntity) = withContext(dispatcher) {
        db.filmDao().deleteFilm(film)
    }

    override suspend fun getAllFilmsFromDb(): List<FilmEntity> = withContext(dispatcher) {
        db.filmDao().getAll()
    }


}