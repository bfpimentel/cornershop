package dev.pimentel.counters.shared.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import dev.pimentel.counters.shared.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    suspend fun navigate(directions: NavDirections)
    suspend fun pop(@IdRes destinationId: Int? = null, inclusive: Boolean = false)
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl(private val dispatchersProvider: DispatchersProvider) : Navigator {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override suspend fun navigate(directions: NavDirections) {
        withContext(dispatchersProvider.ui) { navController?.navigate(directions) }
    }

    override suspend fun pop(@IdRes destinationId: Int?, inclusive: Boolean) {
        withContext(dispatchersProvider.ui) {
            destinationId
                ?.let { destinationId -> navController?.popBackStack(destinationId, inclusive) }
                ?: run { navController?.popBackStack() }
        }
    }
}
