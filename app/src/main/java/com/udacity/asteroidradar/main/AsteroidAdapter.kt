package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidViewItemBinding

class AsteroidAdapter(val onClickListener: OnClickListener) : ListAdapter<Asteroid, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * DiffCallback used to determine when items in the list
     * might have changed from their current positions
     */
    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class AsteroidViewItemHolder private constructor(private val binding: AsteroidViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Asteroid) {
            binding.asteroid = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidViewItemBinding.inflate(layoutInflater, parent, false)

                return AsteroidViewItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AsteroidViewItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val asteroid = getItem(position)
        when (holder) {
            is AsteroidViewItemHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(asteroid)
                }
                holder.bind(asteroid)
            }
        }
    }

    /**
     * Setting a on click listener that each item in the recycler view will have access to
     */
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}