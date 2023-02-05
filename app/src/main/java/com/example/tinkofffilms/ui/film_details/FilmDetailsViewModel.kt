package com.example.tinkofffilms.ui.film_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tinkofffilms.data.repository.IFilmsRepository
import com.example.tinkofffilms.data.repository.Mapper.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class FilmDetailsViewModel(private val repository: IFilmsRepository) : ViewModel() {
    private val _states: MutableStateFlow<FilmDetailsState> = MutableStateFlow(FilmDetailsState.Loading)

    val states: Flow<FilmDetailsState> = _states

    fun loadFilm(id: Int,isFavourite: Boolean) {
        viewModelScope.launch {
            try {
                _states.emit(FilmDetailsState.Loading)
                val item = repository.getFilmInfo(id).toUi(isFavourite)
                _states.emit(FilmDetailsState.Result(item))
            } catch (e: Throwable) {
                Log.d("qwe", "loadFilms: ${e.stackTraceToString()}")
                _states.emit(FilmDetailsState.Error)
            }
        }
    }

    class Factory(private val repo: IFilmsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FilmDetailsViewModel(repo) as T
        }
    }
}
