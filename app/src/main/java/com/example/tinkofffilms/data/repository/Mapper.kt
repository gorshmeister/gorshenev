package com.example.tinkofffilms.data.repository

import com.example.tinkofffilms.data.database.FilmEntity
import com.example.tinkofffilms.data.network.model.FilmDescriptionRemote
import com.example.tinkofffilms.data.network.model.FilmPreviewRemote
import com.example.tinkofffilms.data.network.model.Genre
import com.example.tinkofffilms.ui.film_details.FilmDescriptionUi
import com.example.tinkofffilms.ui.films.FilmPreviewUi

object Mapper {

    private fun List<Genre>.mapNotNullAndCapitalizeFirst(): String =
        this.mapNotNull { it.genre }
            .mapIndexed { i, s ->
                if (i == 0) s.replaceFirstChar(Char::uppercaseChar) else s
            }.joinToString()

    fun FilmDescriptionRemote.toUi(isFavourite: Boolean): FilmDescriptionUi {
        return FilmDescriptionUi(
            id = id,
            nameRu = nameRu.orEmpty(),
            nameEn = nameEn.orEmpty(),
            posterUrl = posterUrl.orEmpty(),
            rating = rating.orEmpty(),
            year = year.orEmpty(),
            filmLength = filmLength.orEmpty(),
            slogan = slogan.orEmpty(),
            description = description.orEmpty(),
            countries = countries.mapNotNull { it.country },
            genres = genres.mapNotNullAndCapitalizeFirst(),
            favourite = isFavourite
        )
    }

    fun FilmPreviewRemote.toUi(): FilmPreviewUi {
        val nameEn = when {
            nameEn.isNullOrBlank() -> year.orEmpty()
            else -> "$nameEn, $year"
        }

        return FilmPreviewUi(
            id = id,
            nameRu = nameRu.orEmpty(),
            nameEn = nameEn,
            year = year.orEmpty(),
            genres = genres.mapNotNullAndCapitalizeFirst(),
            rating = rating.orEmpty(),
            posterUrlPreview = posterUrlPreview.orEmpty(),
            favourite = false
        )
    }

    fun FilmPreviewUi.toEntity(): FilmEntity {
        return FilmEntity(
            id = id,
            nameRu = nameRu,
            nameEn = nameEn,
            year = year,
            genres = genres,
            rating = rating,
            posterUrlPreview = posterUrlPreview,
            favourite = favourite
        )
    }
    fun FilmEntity.toUi(): FilmPreviewUi {
        return FilmPreviewUi(
            id = id,
            nameRu = nameRu,
            nameEn = nameEn,
            year = year,
            genres = genres,
            rating = rating,
            posterUrlPreview = posterUrlPreview,
            favourite = favourite
        )
    }

}