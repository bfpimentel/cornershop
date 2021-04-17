package com.cornershop.counterstest.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.di.PresentationModule.NavigatorBinderQualifier
import com.cornershop.counterstest.shared.navigator.NavigatorBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    @Inject
    @NavigatorBinderQualifier
    lateinit var navigator: NavigatorBinder

    override fun onResume() {
        super.onResume()

        val navController = supportFragmentManager
            .findFragmentById(R.id.navHostFragment)!!
            .findNavController()

        navigator.bind(navController, lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.unbind()
    }
}
