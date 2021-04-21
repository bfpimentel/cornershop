package com.cornershop.counterstest.presentation.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.cornershop.counterstest.databinding.CountersCounterItemBinding
import com.cornershop.counterstest.databinding.CountersEditItemBinding
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CountersAdapter @AssistedInject constructor(
    @Assisted private val listener: CounterListener
) : ListAdapter<CounterViewData, CountersAdapter.ViewHolder>(Diff) {

    override fun getItemViewType(position: Int): Int =
        if (getItem(position) is CounterViewData.Counter) CounterViewData.Counter.IDENTIFIER
        else CounterViewData.Edit.IDENTIFIER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == CounterViewData.Counter.IDENTIFIER) {
            CountViewHolder(CountersCounterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            EditViewHolder(CountersEditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    abstract inner class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: CounterViewData)
    }

    private inner class CountViewHolder(private val binding: CountersCounterItemBinding) : ViewHolder(binding) {

        override fun bind(item: CounterViewData) = with(binding) {
            item as CounterViewData.Counter

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

    private inner class EditViewHolder(private val binding: CountersEditItemBinding) : ViewHolder(binding) {

        override fun bind(item: CounterViewData) = with(binding) {
            item as CounterViewData.Edit

            title.text = item.title
            root.isSelected = item.isSelected
            selectedIcon.isInvisible = !item.isSelected
            root.setOnClickListener { listener.onCounterClick(counterId = item.id) }
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
