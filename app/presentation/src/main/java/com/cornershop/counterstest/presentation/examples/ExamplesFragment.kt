package com.cornershop.counterstest.presentation.examples

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ExamplesFragmentBinding
import com.cornershop.counterstest.presentation.examples.data.ExamplesIntention
import com.cornershop.counterstest.shared.extensions.watch
import com.cornershop.counterstest.shared.mvi.handleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExamplesFragment : DialogFragment(R.layout.examples_fragment) {

    private lateinit var binding: ExamplesFragmentBinding
    private val viewModel: ExamplesContract.ViewModel by viewModels<ExamplesViewModel>()

    @Inject
    lateinit var adapterFactory: ExamplesAdapter.Factory
    private lateinit var examplesAdapter: ExamplesAdapter

    override fun getTheme(): Int = R.style.Theme_CountersExamples

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ExamplesFragmentBinding.bind(view)

        bindAdapter()
        bindOutputs()
        bindInputs()
    }

    private fun bindAdapter() {
        this.examplesAdapter = this.adapterFactory.create { name ->
            viewModel.publish(ExamplesIntention.SelectExample(name))
        }

        binding.examples.let { examples ->
            examples.adapter = examplesAdapter
            examples.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
            state.examplesEvent.handleEvent(examplesAdapter::submitList)
        }
    }

    private fun bindInputs() {
        binding.toolbar.setNavigationOnClickListener { viewModel.publish(ExamplesIntention.Close) }

        viewModel.publish(ExamplesIntention.GetExamples)
    }
}
