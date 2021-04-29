package dev.pimentel.counters.presentation.welcome

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.pimentel.counters.R
import dev.pimentel.counters.databinding.WelcomeFragmentBinding
import dev.pimentel.counters.presentation.welcome.data.WelcomeIntention
import dev.pimentel.counters.shared.extensions.watch
import dev.pimentel.counters.shared.mvi.handleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.welcome_fragment) {

    private lateinit var binding: WelcomeFragmentBinding
    private val viewModel: WelcomeContract.ViewModel by viewModels<WelcomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WelcomeFragmentBinding.bind(view)

        bindOutputs()
        bindInputs()
    }

    private fun bindOutputs() {
        watch(viewModel.state) { state ->
            binding.startButton.isEnabled = state.isButtonEnabled

            state.errorEvent.handleEvent { showErrorDialog() }
        }
    }

    private fun bindInputs() {
        binding.startButton.setOnClickListener { viewModel.publish(WelcomeIntention.NavigateToCounters) }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext(), R.style.Theme_Alert)
            .setMessage(R.string.welcome_error_message)
            .setCancelable(true)
            .setOnCancelListener(DialogInterface::dismiss)
            .setPositiveButton(R.string.welcome_error_button_ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
