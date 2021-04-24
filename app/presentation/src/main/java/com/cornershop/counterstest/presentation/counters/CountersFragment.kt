package com.cornershop.counterstest.presentation.counters

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersFragmentBinding
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.shared.extensions.watch
import com.cornershop.counterstest.shared.mvi.handleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountersFragment : Fragment(R.layout.counters_fragment) {

    private lateinit var binding: CountersFragmentBinding
    private val viewModel: CountersContract.ViewModel by viewModels<CountersViewModel>()

    @Inject
    lateinit var adapterFactory: CountersAdapter.Factory
    private lateinit var countersAdapter: CountersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CountersFragmentBinding.bind(view)

        bindAdapter()
        bindOutputs()
        bindInputs()
    }

    private fun bindAdapter() {
        this.countersAdapter = adapterFactory.create(object : CountersContract.ItemListener {
            override fun onIncreaseClick(counterId: String) {
                viewModel.publish(CountersIntention.Increase(counterId))
            }

            override fun onDecreaseClick(counterId: String) {
                viewModel.publish(CountersIntention.Decrease(counterId))
            }

            override fun onCounterLongClick(counterId: String) {
                viewModel.publish(CountersIntention.StartEditing(counterId))
            }

            override fun onCounterClick(counterId: String) {
                viewModel.publish(CountersIntention.SelectOrDeselectCounter(counterId))
            }
        })

        binding.counters.let { counters ->
            counters.adapter = countersAdapter
            counters.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
            with(binding) {
                totalItemCount.text = getString(R.string.counters_total_items_count, state.totalItemCount)
                totalTimesCount.text = getString(R.string.counters_total_times_count, state.totalTimesCount)
                editingToolbar.title = getString(
                    R.string.counters_editing_toolbar_title,
                    state.numberOfSelectedCounters
                )
                editingToolbar.menu.findItem(R.id.deleteCounters).isEnabled = state.areMenusEnabled
                editingToolbar.menu.findItem(R.id.shareCounters).isEnabled = state.areMenusEnabled

                state.layoutEvent.handleEvent { layout ->
                    toolbarLayout.isVisible = layout.isToolbarVisible
                    searchInputLayout.isVisible = layout.isSearchInputVisible
                }
            }

            state.countersEvent.handleEvent(countersAdapter::submitList)
            state.deleteConfirmationEvent.handleEvent(::showDeleteConfirmationDialog)
            state.shareEvent.handleEvent(::shareCounters)
        }
    }

    private fun bindInputs() {
        with(binding) {
            searchInput.doAfterTextChanged { editable ->
                viewModel.publish(CountersIntention.SearchCounters(query = editable.toString()))
            }

            addCounter.setOnClickListener {
                viewModel.publish(CountersIntention.NavigateToCreateCounter)
            }

            editingToolbar.setNavigationOnClickListener {
                viewModel.publish(CountersIntention.FinishEditing)
            }

            editingToolbar.menu.findItem(R.id.deleteCounters).setOnMenuItemClickListener {
                viewModel.publish(CountersIntention.TryDeleting)
                true
            }

            editingToolbar.menu.findItem(R.id.shareCounters).setOnMenuItemClickListener {
                viewModel.publish(CountersIntention.ShareSelectedCounters)
                true
            }
        }

        viewModel.publish(CountersIntention.SearchCounters())
    }

    private fun showDeleteConfirmationDialog(text: String) {
        AlertDialog.Builder(requireContext(), R.style.Theme_Default_Dialog)
            .setMessage(text)
            .setCancelable(true)
            .setOnCancelListener(DialogInterface::dismiss)
            .setPositiveButton(R.string.counters_delete_confirmation_delete) { _, _ ->
                viewModel.publish(CountersIntention.DeleteSelectedCounters)
            }
            .show()
    }

    private fun shareCounters(text: String) {
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(SHARE_TYPE)
            .setText(text)
            .startChooser()
    }

    private companion object {
        private const val SHARE_TYPE = "text/plain"
    }
}
