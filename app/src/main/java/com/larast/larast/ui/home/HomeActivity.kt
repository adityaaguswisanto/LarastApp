package com.larast.larast.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.larast.larast.R
import com.larast.larast.data.helper.gone
import com.larast.larast.data.helper.launchActivity
import com.larast.larast.data.helper.visible
import com.larast.larast.data.store.UserStore
import com.larast.larast.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navigationHost.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomNavigationView.visible()
                R.id.trackFragment -> bottomNavigationView.visible()
                R.id.familyFragment -> bottomNavigationView.visible()
                R.id.settingsFragment -> bottomNavigationView.visible()
                else -> bottomNavigationView.gone()
            }
        }

        bottomNavigationView.setupWithNavController(navController)

    }

    fun performLogout() = lifecycleScope.launch {
        userStore.clear()
        launchActivity(AuthActivity::class.java)
    }
}