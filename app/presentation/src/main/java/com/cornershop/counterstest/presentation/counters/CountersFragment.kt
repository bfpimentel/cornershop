package com.cornershop.counterstest.presentation.counters

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cornershop.counterstest.databinding.CountersFragmentBinding
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.shared.extensions.viewBinding
import com.cornershop.counterstest.shared.extensions.watch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountersFragment : Fragment() {

    private val binding by viewBinding(CountersFragmentBinding::bind)
    private val viewModel: CountersContract.ViewModel by viewModels<CountersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInputs()
        bindOutputs()
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
        }
    }

    private fun bindInputs() {
        Log.d("GET_COUNTERS", "TEST")
        viewModel.publish(CountersIntention.GetCounters)
    }
}
