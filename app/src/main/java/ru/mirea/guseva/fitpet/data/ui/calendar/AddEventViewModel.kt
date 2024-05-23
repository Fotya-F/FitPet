package ru.mirea.guseva.fitpet.data.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Event
import ru.mirea.guseva.fitpet.data.repository.EventRepository

class AddEventViewModel(application: Application) : AndroidViewModel(application) {
    private val eventDao = AppDatabase.getDatabase(application).eventDao()
    private val repository: EventRepository = EventRepository(eventDao)

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch {
        repository.update(event)
    }

    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }
}
