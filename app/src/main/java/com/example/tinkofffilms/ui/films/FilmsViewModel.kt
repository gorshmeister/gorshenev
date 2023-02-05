package com.example.tinkofffilms.ui.films

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tinkofffilms.data.repository.IFilmsRepository
import com.example.tinkofffilms.data.repository.Mapper.toEntity
import com.example.tinkofffilms.data.repository.Mapper.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmsViewModel(private val repository: IFilmsRepository) : ViewModel() {
    private val _states: MutableStateFlow<FilmsState> = MutableStateFlow(FilmsState.Loading)

    val states: Flow<FilmsState> = _states

    private val _search: MutableStateFlow<String> = MutableStateFlow("")

    val cachedItems: MutableList<FilmPreviewUi> = mutableListOf()

    init {
        _search
            .debounce(500)
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
            .onEach { query -> initSearch(query) }
            .launchIn(viewModelScope)
    }

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch() {
            _states.emit(FilmsState.Loading)
            try {
                loadRemoteFilmsAsync().await()
                loadLocaleFilmsAsync().await()
            } catch (e: Throwable) {
                Log.d("qwe", "loadFilms: ${e.stackTraceToString()}")
                _states.emit(FilmsState.Error)
                loadLocaleFilmsAsync().await()
            }
        }
    }

    private fun loadLocaleFilmsAsync() =
        viewModelScope.async() {
            val local = repository.getAllFilmsFromDb().map { it.toUi() }
            if (cachedItems.isNotEmpty()) {
                withContext(Dispatchers.Default) {
                    cachedItems.map { cache ->
                        if (local.any { loc -> loc.id == cache.id }) cache.copy(favourite = true)
                        else cache
                    }.also {
                        cachedItems.clear()
                        cachedItems.addAll(it)
                    }
                }
                _states.emit(FilmsState.Result(cachedItems))
            } else if (local.isNotEmpty()) {
                _states.emit(FilmsState.Result(local))
                cachedItems.addAll(local)
            } else _states.emit(FilmsState.Error)
        }


    private fun loadRemoteFilmsAsync() =
        viewModelScope.async() {
            val remote = repository.getTopFilms().map { it.toUi() }
            cachedItems.addAll(remote)
            _states.emit(FilmsState.Result(remote))
        }


    fun addToDb(film: FilmPreviewUi) {
        viewModelScope.launch {
            repository.addToDb(film.toEntity())
        }
    }

    fun deleteFromDb(film: FilmPreviewUi) {
        viewModelScope.launch {
            repository.deleteFromDb(film.toEntity())
        }
    }

    fun search(userInput: String) {
        _search.value = userInput
    }

    private suspend fun initSearch(query: String) {
        if (query.isNotBlank()) {
            val searchResult = cachedItems.filter { item ->
                val nameRuContainsQuery = item.nameRu.contains(query, true)
                val nameEnContainsQuery = item.nameEn.contains(query, true)
                val genresContainsQuery = item.genres.contains(query, true)
                nameRuContainsQuery || nameEnContainsQuery || genresContainsQuery
            }
            _states.emit(FilmsState.Result(searchResult))
        } else if (cachedItems.isNotEmpty()) {
            _states.emit(FilmsState.Result(cachedItems))
        }
    }


    class Factory(private val repo: IFilmsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FilmsViewModel(repo) as T
        }
    }
}



