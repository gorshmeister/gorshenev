package com.example.tinkofffilms.ui.film_details

sealed class FilmDetailsState {
    object Loading: FilmDetailsState()
    object Error: FilmDetailsState()
    data class Result(val item: FilmDescriptionUi): FilmDetailsState()
}

