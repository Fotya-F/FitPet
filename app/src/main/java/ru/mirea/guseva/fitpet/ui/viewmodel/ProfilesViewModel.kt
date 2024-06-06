package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.PetRepository
import javax.inject.Inject

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {
    val pets = petRepository.allPets.asLiveData()

    fun syncWithFirestore() {
        viewModelScope.launch {
            // petRepository.syncWithFirestore()
        }
    }
}
