package com.example.tinkofffilms.ui.films.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkofffilms.R
import com.example.tinkofffilms.ui.films.FilmPreviewUi
import com.example.tinkofffilms.utils.ItemDiffUtil

class FilmsAdapter(
    private val onClick: (FilmPreviewUi) -> Unit,
    private val onLongClick: (FilmPreviewUi) -> Unit
) : RecyclerView.Adapter<FilmsViewHolder>() {

    private val localItems: MutableList<FilmPreviewUi> = mutableListOf()

    var items: List<FilmPreviewUi>
        get() = localItems
        set(newItems) {
            val itemDiffUtilCallback = ItemDiffUtil(localItems, newItems)
            val itemDiffResult = DiffUtil.calculateDiff(itemDiffUtilCallback, true)
            localItems.clear()
            localItems.addAll(newItems)
            itemDiffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return FilmsViewHolder(view).apply {
            view.setOnClickListener { onClick(items[bindingAdapterPosition]) }
            view.setOnLongClickListener {
                onLongClick(items[bindingAdapterPosition])
                true
            }
        }
    }

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}