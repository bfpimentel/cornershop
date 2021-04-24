package com.cornershop.counterstest.data.generator

import java.util.Random

/**
 * @author https://gist.github.com/AllanHasegawa/d1e251744275d16af45484b0dbac37ee
 * */
private const val PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"
private val DEFAULT_INSTANT = { System.currentTimeMillis() }

private var globalState = IdGenerator.State()

interface IdGenerator {
    suspend fun generateNextId(previousState: State, instant: Long = DEFAULT_INSTANT()): Result
    suspend fun generateId(instant: Long = DEFAULT_INSTANT()): String

    data class State(
        val lastInstant: Long = -1L,
        val lastRandChars: IntArray = IntArray(12)
    ) {
        override fun equals(other: Any?) = lastInstant == (other as? State)?.lastInstant

        override fun hashCode() = lastInstant.toInt()
    }

    data class Result(val id: String, val nextState: State)
}

class IdGeneratorImpl : IdGenerator {

    override suspend fun generateNextId(previousState: IdGenerator.State, instant: Long): IdGenerator.Result {
        val duplicateTime = (instant == previousState.lastInstant)

        val timeStampChars = CharArray(8).also { arr ->
            var instantLeft = instant
            (7 downTo 0).forEach {
                val module = instantLeft % 64L
                instantLeft /= 64L
                arr[it] = PUSH_CHARS[module.toInt()]
            }
            if (instantLeft != 0L) {
                throw AssertionError("We should have converted the entire timestamp.")
            }
        }

        val randChars = when (!duplicateTime) {
            true -> Random().let { r -> IntArray(12) { r.nextInt(64) } }
            else -> previousState.lastRandChars.copyOf()
                .also { arr ->
                    val lastNot63 = arr.indexOfLast { it != 63 }
                    arr.fill(element = 0, fromIndex = lastNot63 + 1)
                    arr[lastNot63]++
                }
        }

        val randCharsAsString = randChars.fold(StringBuilder(12)) { str, i -> str.append(PUSH_CHARS[i]) }

        val id = String(timeStampChars) + randCharsAsString
        require(id.length == 20) { "Length should be 20." }

        return IdGenerator.Result(
            id = id,
            nextState = IdGenerator.State(lastInstant = instant, lastRandChars = randChars)
        )
    }

    override suspend fun generateId(instant: Long): String {
        val operation = suspend {
            generateNextId(globalState, instant)
                .let { result ->
                    globalState = result.nextState
                    result.id
                }
        }

        return operation()
    }
}
