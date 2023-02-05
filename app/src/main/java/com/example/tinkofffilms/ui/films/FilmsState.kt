package com.example.tinkofffilms.ui.films

sealed class FilmsState {
    object Loading: FilmsState()
    object Error: FilmsState()
    data class Result(val items: List<FilmPreviewUi>): FilmsState()
}

