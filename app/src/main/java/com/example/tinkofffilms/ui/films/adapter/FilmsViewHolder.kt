package com.example.tinkofffilms.ui.films.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinkofffilms.databinding.ItemFilmBinding
import com.example.tinkofffilms.ui.films.FilmPreviewUi

class FilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding: ItemFilmBinding = ItemFilmBinding.bind(itemView)

    fun bind(item: FilmPreviewUi) {
        with(binding) {
            Glide.with(itemView).load(item.posterUrlPreview).into(image)
            name.text = item.nameRu
            nameEn.text = item.nameEn
            genre.text = item.genres
            favourite.isVisible = item.favourite
        }
    }
}
