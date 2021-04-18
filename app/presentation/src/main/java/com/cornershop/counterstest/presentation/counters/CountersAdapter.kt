package com.cornershop.counterstest.presentation.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.CountersItemBinding
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CountersAdapter @AssistedInject constructor(
    @Assisted private val listener: CounterListener
) : ListAdapter<CounterViewData, CountersAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        binding = CountersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: CountersItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CounterViewData) = with(binding) {
            title.text = item.title
            count.text = item.count.toString()
            plus.setOnClickListener { listener.onAddClick(counterId = item.id) }
            minus.setOnClickListener { listener.onSubtractClick(counterId = item.id) }
            root.setOnLongClickListener {
                listener.onCounterLongClick(counterId = item.id)
                return@setOnLongClickListener true
            }
        }
    }

    companion object Diff : DiffUtil.ItemCallback<CounterViewData>() {
        override fun areItemsTheSame(oldItem: CounterViewData, newItem: CounterViewData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CounterViewData, newItem: CounterViewData): Boolean =
            oldItem == newItem
    }
}

@AssistedFactory
interface CountersAdapterFactory {
    fun create(listener: CounterListener): CountersAdapter
}

interface CounterListener {
    fun onCounterLongClick(counterId: String)
    fun onCounterClick(counterId: String)
    fun onAddClick(counterId: String)
    fun onSubtractClick(counterId: String)
}
