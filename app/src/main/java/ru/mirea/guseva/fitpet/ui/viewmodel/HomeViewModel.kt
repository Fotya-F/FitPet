package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.EventRepository
import ru.mirea.guseva.fitpet.data.PetRepository
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    val pets: LiveData<List<Pet>> = petRepository.allPets.asLiveData()
    val events: LiveData<List<Event>> = eventRepository.getAllEvents().asLiveData()

    fun getEventsByPet(petId: Int): LiveData<List<Event>> {
        return eventRepository.getEventsByPet(petId).asLiveData()
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            petRepository.syncWithFirestore()
            eventRepository.syncWithFirestore()
        }
    }


    fun getSleepValue(petId: Int): Float {
        return petRepository.getSleepValue(petId)
    }

    fun getWeightValue(petId: Int): Float {
        return petRepository.getWeightValue(petId)
    }

    fun getActivityValue(petId: Int): Float {
        return petRepository.getActivityValue(petId)
    }
}
