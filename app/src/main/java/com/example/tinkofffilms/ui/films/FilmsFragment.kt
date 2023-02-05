package com.example.tinkofffilms.ui.films

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.tinkofffilms.MainActivity
import com.example.tinkofffilms.R
import com.example.tinkofffilms.databinding.FragmentFilmsBinding
import com.example.tinkofffilms.di.ServiceLocator
import com.example.tinkofffilms.ui.films.adapter.FilmsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FilmsFragment : Fragment(R.layout.fragment_films) {
    private val binding: FragmentFilmsBinding by viewBinding()

    private val viewModel: FilmsViewModel by viewModels {
        FilmsViewModel.Factory(ServiceLocator.repository)
    }

    private val adapter: FilmsAdapter = FilmsAdapter(::onClick, ::onLongClick)

    private var lastOpenedFilmId: Int = UNKNOWN_FILM_ID


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastOpenedFilmId = savedInstanceState?.getInt(ID, UNKNOWN_FILM_ID) ?: UNKNOWN_FILM_ID
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) onClick(viewModel.cachedItems.find { it.id == lastOpenedFilmId }?: viewModel.cachedItems[0])

        with(binding) {
            rvFilms.adapter = adapter
            initButtons()
            initSearchField()
        }
        viewModel.states.onEach(::render).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ID, lastOpenedFilmId)
    }

    private fun render(state: FilmsState) {
        with(binding) {
            when (state) {
                FilmsState.Error -> {
                    content.isVisible = false
                    errorScreen.root.isVisible = true
                    progressBar.isVisible = false
                }
                FilmsState.Loading -> {
                    content.isVisible = false
                    errorScreen.root.isVisible = false
                    progressBar.isVisible = true
                }
                is FilmsState.Result -> {
                    adapter.items = state.items
                    content.isVisible = true
                    errorScreen.root.isVisible = false
                    progressBar.isVisible = false
                    btnNotFound.isVisible = adapter.items.isEmpty()
                }
            }
        }
    }

    private fun initButtons() {
        with(binding) {
            errorScreen.btnRepeat.setOnClickListener { viewModel.loadFilms() }

            with(buttons) {
                btnFavourite.setOnClickListener {
                    adapter.items = adapter.items.filter { it.favourite }
                }
                btnPopular.setOnClickListener {
                    if (viewModel.cachedItems.isNotEmpty())
                        adapter.items = viewModel.cachedItems
                }
            }
        }
    }

    private fun initSearchField() {
        with(binding) {
            with(searchField) {
                etSearch.addTextChangedListener { query ->
                    viewModel.search(query?.toString().orEmpty())

                    if (query.toString().isNotBlank()) {
                        ivClose.isVisible = true
                        ivSearch.isVisible = false
                        ivClose.setOnClickListener { etSearch.text.clear() }
                    } else {
                        ivClose.isVisible = false
                        ivSearch.isVisible = true
                    }
                }
            }
        }
    }

    private fun onClick(film: FilmPreviewUi) {
        lastOpenedFilmId = film.id
        requireActivity().supportFragmentManager.setFragmentResult(
            MainActivity.FRAGMENT_RESULT_KEY,
            bundleOf(ID to film.id)
        )
    }

    private fun onLongClick(film: FilmPreviewUi) {
        adapter.items = adapter.items.map { item ->
            val haveSameIds = item.id == film.id
            val isFavourite = item.favourite

            if (haveSameIds && !isFavourite) {
                item.copy(favourite = true).also { viewModel.addToDb(it) }
            } else if (haveSameIds) {
                item.copy(favourite = false).also { viewModel.deleteFromDb(it) }
            } else item
        }
        viewModel.cachedItems.apply {
            this.clear()
            this.addAll(adapter.items)
        }
    }

    companion object {
        const val UNKNOWN_FILM_ID = -1
        const val TAG = "FilmsFragment"
        const val ID = "id"
        const val IS_FAVOURITE = "is_favourite"
    }

}