package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.EventRepository
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun getEventsByPet(petId: Int) = eventRepository.getEventsByPetAndUser(petId, userId).asLiveData()

    val allEvents = eventRepository.getAllEventsByUser(userId).asLiveData()

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.insertEvent(event.copy(userId = userId))
        }
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            eventRepository.syncWithFirestore()
        }
    }

    fun restoreFromFirestore() {
        viewModelScope.launch {
            eventRepository.restoreFromFirestore()
        }
    }
}
