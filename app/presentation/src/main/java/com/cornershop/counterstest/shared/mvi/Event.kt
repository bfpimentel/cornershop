package com.cornershop.counterstest.shared.mvi

data class Event<T>(private val content: T) {

    private var hasBeenHandled = false

    val value: T?
        get() {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }
}

fun <T> T.toEvent() = Event(this)

fun <T> Event<T>?.handleEvent(block: (T) -> Unit) {
    this?.value?.let(block)
}
