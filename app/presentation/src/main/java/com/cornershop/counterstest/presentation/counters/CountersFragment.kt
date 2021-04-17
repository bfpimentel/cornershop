package com.cornershop.counterstest.presentation.counters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cornershop.counterstest.databinding.CountersFragmentBinding
import com.cornershop.counterstest.shared.extensions.viewBinding
import com.cornershop.counterstest.shared.extensions.watch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CountersFragment : Fragment() {

    private val binding by viewBinding(CountersFragmentBinding::bind)
    private val viewModel: CountersContract.ViewModel by viewModel<CountersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(countersModule)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unloadKoinModules(countersModule)
    }

    private fun bindInputs() {

    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
        }
    }
}
