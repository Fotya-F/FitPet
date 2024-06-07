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

    // Используем LiveData для наблюдения за изменениями в списке питомцев
    val pets = petRepository.getAllPetsByUser(userId).asLiveData()

    // Получение питомца по ID
    fun getPetById(petId: Int) = petRepository.getPetById(petId).asLiveData()

    // Вставка нового питомца
    fun insertPet(pet: Pet) {
        viewModelScope.launch {
            petRepository.insertPet(pet)
        }
    }

    // Обновление данных питомца
    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.updatePet(pet)
        }
    }

    // Удаление питомца
    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.deletePet(pet)
        }
    }

    // Получение питомца по имени
    fun getPetByName(petName: String): Pet? = runBlocking {
        petRepository.getPetByName(petName)
    }

    // Синхронизация данных с Firestore
    fun syncWithFirestore() {
        viewModelScope.launch {
            petRepository.syncWithFirestore()
        }
    }

    // Восстановление данных из Firestore
    fun restoreFromFirestore() {
        viewModelScope.launch {
            petRepository.restoreFromFirestore()
        }
    }
}
