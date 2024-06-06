package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val pets = petRepository.getAllPetsByUser(userId).asLiveData()

    fun getPetById(petId: Int) = petRepository.getPetById(petId).asLiveData()

    fun insertPet(pet: Pet) {
        viewModelScope.launch {
            petRepository.insertPet(pet)
        }
    }

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
