package ru.mirea.guseva.fitpet.data.ui.care

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Pet
import ru.mirea.guseva.fitpet.data.repository.PetRepository
import kotlinx.coroutines.launch

class CareViewModel(application: Application) : AndroidViewModel(application) {
    private val petDao = AppDatabase.getDatabase(application).petDao()
    private val repository: PetRepository = PetRepository(petDao)
    val allPets: LiveData<List<Pet>> = repository.allPets

    fun insert(pet: Pet) = viewModelScope.launch {
        repository.insert(pet)
    }

    fun update(pet: Pet) = viewModelScope.launch {
        repository.update(pet)
    }

    fun delete(pet: Pet) = viewModelScope.launch {
        repository.delete(pet)
    }
}
