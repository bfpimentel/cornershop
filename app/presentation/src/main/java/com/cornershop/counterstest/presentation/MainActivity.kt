package com.cornershop.counterstest.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.di.presentationModules
import com.cornershop.counterstest.presentation.shared.navigator.Navigator
import com.cornershop.counterstest.presentation.shared.navigator.NavigatorBinder
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val navigator: NavigatorBinder by inject<Navigator>()

    private val modules = presentationModules

    override fun onResume() {
        super.onResume()
        loadKoinModules(modules)

        val navController = supportFragmentManager
            .findFragmentById(R.id.navHostFragment)!!
            .findNavController()

        navigator.bind(navController, lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.unbind()
        unloadKoinModules(modules)
    }
}
