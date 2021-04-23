package com.cornershop.counterstest.presentation.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.WelcomeFragmentBinding
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.welcome_fragment) {

    private val viewModel: WelcomeContract.ViewModel by viewModels<WelcomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = WelcomeFragmentBinding.bind(view)
        binding.startButton.setOnClickListener { viewModel.publish(WelcomeIntention.NavigateToCounters) }
    }
}
