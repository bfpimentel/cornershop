package com.cornershop.counterstest.shared.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.viewBinding(bindingFactory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        init {
            this@viewBinding.viewLifecycleOwnerLiveData.observe(this@viewBinding) { owner ->
                owner?.lifecycle?.addObserver(this)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: bindingFactory(requireView()).also { newBinding ->
                binding = newBinding
            }
    }

inline fun <T> Fragment.watch(source: StateFlow<T>, crossinline block: (T) -> Unit) {
    lifecycleScope.launchWhenCreated { source.collect { block(it) } }
}
