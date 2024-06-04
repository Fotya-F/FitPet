package ru.mirea.guseva.fitpet

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.ui.viewmodel.AuthViewModel
import ru.mirea.guseva.fitpet.utils.NotificationHelper

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupNavigation()

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)

        // Initialize the database
        val applicationScope = CoroutineScope(SupervisorJob())
        AppDatabase.getDatabase(applicationContext, applicationScope)

        authViewModel.currentUser.observe(this) { firebaseUser ->
            // Handling the navigation based on the authentication state
            if (firebaseUser != null && navController.currentDestination?.id == R.id.authFragment) {
                // User is logged in, navigate to main_graph
                navController.navigate(R.id.action_authFragment_to_main_graph)
            } else if (firebaseUser == null && navController.currentDestination?.id != R.id.authFragment) {
                // User is not logged in, navigate to auth_graph
                navController.navigate(R.id.auth_graph)
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        bottomNavView = findViewById(R.id.bottom_nav_view)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Handling the visibility of the BottomNavigationView
            bottomNavView.visibility = if (destination.id == R.id.authFragment) View.GONE else View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
