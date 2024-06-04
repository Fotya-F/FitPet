package ru.mirea.guseva.fitpet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FitPetApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Никакие дополнительные настройки для Firebase Auth здесь не требуются
    }
}
