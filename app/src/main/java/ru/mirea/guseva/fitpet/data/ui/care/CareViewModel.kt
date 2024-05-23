package ru.mirea.guseva.fitpet.data.ui.care

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Pet
import ru.mirea.guseva.fitpet.data.repository.PetRepository

class CareViewModel(application: Application) : AndroidViewModel(application) {
    private val petDao = AppDatabase.getDatabase(application).petDao()
    private val repository: PetRepository = PetRepository(petDao)
    val allPets: LiveData<List<Pet>> = repository.allPets
}
