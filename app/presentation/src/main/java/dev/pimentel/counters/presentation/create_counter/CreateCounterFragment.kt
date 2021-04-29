package dev.pimentel.counters.presentation.create_counter

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dev.pimentel.counters.R
import dev.pimentel.counters.databinding.CreateCounterFragmentBinding
import dev.pimentel.counters.presentation.create_counter.data.CreateCounterIntention
import dev.pimentel.counters.shared.extensions.watch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCounterFragment : DialogFragment(R.layout.create_counter_fragment) {

    private lateinit var binding: CreateCounterFragmentBinding
    private val viewModel: CreateCounterContract.ViewModel by viewModels<CreateCounterViewModel>()

    override fun getTheme(): Int = R.style.Theme_CreateCounter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateCounterFragmentBinding.bind(view)

        bindHintView()
        bindOutputs()
        bindInputs()
    }

    private fun bindHintView() {
        binding.hint.apply {
            linksClickable = true
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()

            val text = SpannableString(getString(R.string.create_counter_disclaimer))

            val exampleSpan = getString(R.string.create_counter_disclaimer_example_span)
            val startIndex = text.indexOf(exampleSpan)
            val endIndex = startIndex + exampleSpan.length

            text.setSpan(object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.color = resources.getColor(R.color.gray, null)
                    textPaint.isUnderlineText = true
                }

                override fun onClick(widget: View) {
                    viewModel.publish(CreateCounterIntention.NavigateToExamples)
                }
            }, startIndex, endIndex, 0)

            setText(text)
        }
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
