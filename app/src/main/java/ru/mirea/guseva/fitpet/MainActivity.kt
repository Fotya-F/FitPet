package ru.mirea.guseva.fitpet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.ui.viewmodel.AuthViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.FeedViewModel
import ru.mirea.guseva.fitpet.utils.NotificationHelper

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupNavigation()

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)

        // Initialize the database
        val applicationScope = CoroutineScope(SupervisorJob())
        AppDatabase.getDatabase(applicationContext, applicationScope)

        authViewModel.currentUser.observe(this) { firebaseUser ->
            currentUser = firebaseUser
            if (firebaseUser != null) {
                // User is logged in
                if (navController.currentDestination?.id == R.id.authFragment) {
                    navController.navigate(R.id.action_authFragment_to_main_graph)
                }
                checkAdminAndAddSampleArticles()
                syncDataWithFirestore()
            } else {
                // User is not logged in
                if (navController.currentDestination?.id != R.id.authFragment) {
                    navController.navigate(R.id.auth_graph)
                }
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

    private fun syncDataWithFirestore() {
        currentUser?.let {
            try {
                feedViewModel.syncAndLoadArticles()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error syncing articles: ${e.message}")
                e.printStackTrace()
            }
        } ?: run {
            Log.d("MainActivity", "User is not logged in, skipping sync.")
        }
    }

    private fun checkAdminAndAddSampleArticles() {
        currentUser?.let {
            try {
                auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
                    val isAdmin = result.claims["admin"] as Boolean? ?: false
                    if (isAdmin) {
                        feedViewModel.clearAndLoadArticles()
                    } else {
                        Toast.makeText(this, "You don't have admin rights to add sample articles.", Toast.LENGTH_SHORT).show()
                    }
                }?.addOnFailureListener {
                    Log.e("MainActivity", "Error getting user claims: ${it.message}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error checking admin claims: ${e.message}")
                e.printStackTrace()
            }
        } ?: run {
            Log.d("MainActivity", "User is not logged in, skipping admin check.")
        }
    }
}
