package ru.mirea.guseva.fitpet.ui.viewmodels

import androidx.lifecycle.ViewModel
import ru.mirea.guseva.fitpet.repository.PetRepository

class HomeViewModel(private val petRepository: PetRepository) : ViewModel() {
    val pets = petRepository.getAllPets()
}
