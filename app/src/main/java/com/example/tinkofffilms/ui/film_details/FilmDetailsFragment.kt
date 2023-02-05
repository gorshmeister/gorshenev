package com.example.tinkofffilms.ui.film_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.tinkofffilms.R
import com.example.tinkofffilms.databinding.FragmentFilmDetailsBinding
import com.example.tinkofffilms.di.ServiceLocator
import com.example.tinkofffilms.ui.films.FilmsFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilmDetailsFragment : Fragment(R.layout.fragment_film_details) {
    private val binding: FragmentFilmDetailsBinding by viewBinding()

    private val viewModel: FilmDetailsViewModel by viewModels {
        FilmDetailsViewModel.Factory(ServiceLocator.repository)
    }

    private val filmId: Int by lazy { arguments?.getInt(FilmsFragment.ID) ?: 0 }
    private val isFavourite: Boolean by lazy {
        arguments?.getBoolean(FilmsFragment.IS_FAVOURITE) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            errorScreen.btnRepeat.setOnClickListener { viewModel.loadFilm(filmId, isFavourite) }
        }
        viewModel.loadFilm(filmId, isFavourite)
        viewModel.states.onEach(::render).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(state: FilmDetailsState) {
        with(binding) {
            when (state) {
                FilmDetailsState.Error -> {
                    content.isVisible = false
                    errorScreen.root.isVisible = true
                    progressBar.isVisible = false
                }
                FilmDetailsState.Loading -> {
                    content.isVisible = false
                    errorScreen.root.isVisible = false
                    progressBar.isVisible = true
                }
                is FilmDetailsState.Result -> {
                    renderFilm(state.item)
                    content.isVisible = true
                    errorScreen.root.isVisible = false
                    progressBar.isVisible = false
                }
            }
        }
    }

    private fun renderFilm(item: FilmDescriptionUi) {
        with(binding) {
            Glide.with(this@FilmDetailsFragment).load(item.posterUrl).into(image)
            name.text = item.nameRu
            description.text = item.description
            genres.text = item.genres
            countries.text = item.countries.joinToString()
        }
    }
}