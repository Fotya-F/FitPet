package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.mirea.guseva.fitpet.data.PetRepository
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {
    val pets = petRepository.allPets.asLiveData()

    fun insertPet(pet: Pet) {
        viewModelScope.launch {
            petRepository.insertPet(pet)
        }
    }

    fun getPetById(petId: Int) = petRepository.getPetById(petId).asLiveData()

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.updatePet(pet)
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.deletePet(pet)
        }
    }

    fun getPetByName(petName: String): Pet? = runBlocking {
        petRepository.getPetByName(petName)
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            // petRepository.syncWithFirestore()
        }
    }
}
