package com.example.cocktails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cocktails.R
import com.example.cocktails.databinding.ItemViewBinding
import com.example.cocktails.model.CocktailModel

class CocktailsAdapter(val listener: clickListener) :
    ListAdapter<CocktailModel, CocktailsAdapter.ViewHolder>(CocktailsDiffUtil()) {
    private var list: ArrayList<CocktailModel> = ArrayList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemViewBinding.bind(view)
        fun bind(item: CocktailModel, listener: clickListener) = with(binding)
        {
            imageCocktail.load(item.imgPath)
            {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
            textCocktail.text = item.name
            itemView.setOnClickListener { listener.onItemClick(item, absoluteAdapterPosition) }
        }

    }

    class CocktailsDiffUtil : DiffUtil.ItemCallback<CocktailModel>() {
        override fun areItemsTheSame(oldItem: CocktailModel, newItem: CocktailModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CocktailModel, newItem: CocktailModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface clickListener {
        fun onItemClick(cocktail: CocktailModel, position: Int)
    }
}