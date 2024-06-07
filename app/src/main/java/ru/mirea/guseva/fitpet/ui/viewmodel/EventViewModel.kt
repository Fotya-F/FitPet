package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.EventRepository
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    fun getEventById(eventId: Int): LiveData<Event?> {
        return eventRepository.getEventById(eventId).asLiveData()
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.updateEvent(event)
        }
    }


    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.delete(event)
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
