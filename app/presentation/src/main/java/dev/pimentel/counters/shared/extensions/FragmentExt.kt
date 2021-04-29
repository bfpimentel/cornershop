package dev.pimentel.counters.shared.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

inline fun <T> Fragment.watch(source: StateFlow<T>, crossinline block: (T) -> Unit) {
    lifecycleScope.launchWhenCreated { source.collect { block(it) } }
}
