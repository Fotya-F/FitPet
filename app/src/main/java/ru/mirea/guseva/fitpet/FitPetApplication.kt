package ru.mirea.guseva.fitpet

import android.app.Application
import ru.mirea.guseva.fitpet.data.ui.utils.NotificationHelper

class FitPetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
