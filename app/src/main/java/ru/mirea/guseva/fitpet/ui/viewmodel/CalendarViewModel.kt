package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.EventRepository
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val allEvents: Flow<List<Event>> = eventRepository.getAllEvents()

    fun deleteOldEvents() {
        viewModelScope.launch {
            eventRepository.deleteOldEvents()
        }
    }

    fun insert(event: Event) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }
    }
}
