package com.cornershop.counterstest.presentation.examples

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ExamplesItemBinding
import com.cornershop.counterstest.presentation.examples.data.ExampleViewData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ExamplesAdapter @AssistedInject constructor(
    @Assisted private val listener: ExamplesContract.ItemListener
) : ListAdapter<ExampleViewData, ExamplesAdapter.ViewHolder>(Diff) {

    @AssistedFactory
    interface Factory {
        fun create(listener: ExamplesContract.ItemListener): ExamplesAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ExamplesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ExamplesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExampleViewData) = with(binding) {

        }
    }

    private companion object Diff : DiffUtil.ItemCallback<ExampleViewData>() {
        override fun areItemsTheSame(oldItem: ExampleViewData, newItem: ExampleViewData): Boolean =
            oldItem.title == oldItem.title

        override fun areContentsTheSame(oldItem: ExampleViewData, newItem: ExampleViewData): Boolean =
            oldItem.examples == oldItem.examples
    }
}
