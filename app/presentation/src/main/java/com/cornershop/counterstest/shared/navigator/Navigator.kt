package com.cornershop.counterstest.shared.navigator

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    suspend fun navigate(directions: NavDirections)
    suspend fun replace(directions: NavDirections)
    suspend fun pop()
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
        withContext(dispatchersProvider.ui) {
            navController?.navigate(directions)
        }
    }

    override suspend fun replace(directions: NavDirections) {
        pop()
        navigate(directions)
    }

    override suspend fun pop() {
        withContext(dispatchersProvider.ui) {
            navController?.popBackStack()
        }
    }
}
