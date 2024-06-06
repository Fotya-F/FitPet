package ru.mirea.guseva.fitpet.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val _currentUser = MutableLiveData<FirebaseUser?>().apply { value = auth.currentUser }
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
            Log.d("AuthViewModel", "Auth state changed: ${firebaseAuth.currentUser?.email}")
        }
    }

    fun registerUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _currentUser.value = auth.currentUser
                saveCredentials(email, password)
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _currentUser.value = auth.currentUser
                saveCredentials(email, password)
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    fun logoutUser() {
        auth.signOut()
        _currentUser.value = null
        clearCredentials()
        Log.d("AuthViewModel", "User logged out and credentials cleared")
    }

    private fun saveCredentials(email: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
        Log.d("AuthViewModel", "Credentials saved: $email")
    }

    private fun clearCredentials() {
        with(sharedPreferences.edit()) {
            remove("email")
            remove("password")
            apply()
        }
    }

    fun getSavedCredentials(): Pair<String?, String?> {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        Log.d("AuthViewModel", "Retrieved credentials: $email, $password")
        return Pair(email, password)
    }
}
