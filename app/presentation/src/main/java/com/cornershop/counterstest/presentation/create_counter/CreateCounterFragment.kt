package com.cornershop.counterstest.presentation.create_counter

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CreateCounterFragmentBinding
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterIntention
import com.cornershop.counterstest.shared.extensions.viewBinding
import com.cornershop.counterstest.shared.extensions.watch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCounterFragment : Fragment(R.layout.create_counter_fragment) {

    private val binding by viewBinding(CreateCounterFragmentBinding::bind)
    private val viewModel: CreateCounterContract.ViewModel by viewModels<CreateCounterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOutputs()
        bindInputs()
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
            binding.toolbar.menu.findItem(R.id.saveCounter).isEnabled = state.canSave
        }
    }

    private fun bindInputs() = with(binding) {
        nameInput.doAfterTextChanged { editable ->
            viewModel.publish(CreateCounterIntention.SetName(name = editable.toString()))
        }

        toolbar.setNavigationOnClickListener {
            viewModel.publish(CreateCounterIntention.Close)
        }

        toolbar.menu.findItem(R.id.saveCounter).setOnMenuItemClickListener {
            viewModel.publish(CreateCounterIntention.Save)
            true
        }
    }
}
