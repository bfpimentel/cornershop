package com.cornershop.counterstest.presentation.counters

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersFragmentBinding
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.shared.extensions.viewBinding
import com.cornershop.counterstest.shared.extensions.watch
import com.cornershop.counterstest.shared.mvi.handleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountersFragment : Fragment(R.layout.counters_fragment) {

    private val binding by viewBinding(CountersFragmentBinding::bind)
    private val viewModel: CountersContract.ViewModel by viewModels<CountersViewModel>()

    @Inject
    lateinit var adapterFactory: CountersAdapterFactory
    private lateinit var countersAdapter: CountersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAdapter()
        bindInputs()
        bindOutputs()
    }

    private fun bindAdapter() {
        this.countersAdapter = adapterFactory.create(object : CounterListener {
            override fun onAddClick(counterId: String) {
                viewModel.publish(CountersIntention.Add(counterId))
            }

            override fun onSubtractClick(counterId: String) {
                viewModel.publish(CountersIntention.Subtract(counterId))
            }

            override fun onCounterLongClick(counterId: String) = Unit

            override fun onCounterClick(counterId: String) = Unit
        })

        binding.counters.let { counters ->
            counters.adapter = countersAdapter
            counters.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun bindInputs() {
        binding.searchInput.doAfterTextChanged { editable ->
            viewModel.publish(CountersIntention.SearchCounters(query = editable.toString()))
        }

        viewModel.publish(CountersIntention.SearchCounters())
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
            with(binding) {
                totalItemCount.text = getString(R.string.counters_total_items_count, state.totalItemCount)
                totalTimesCount.text = getString(R.string.counters_total_times_count, state.totalTimesCount)
            }

            state.countersEvent?.handleEvent { counters ->
                countersAdapter.submitList(counters)
            }
        }
    }
}
