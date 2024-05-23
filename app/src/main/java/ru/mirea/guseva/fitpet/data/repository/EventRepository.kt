package ru.mirea.guseva.fitpet.data.repository

import androidx.lifecycle.LiveData
import ru.mirea.guseva.fitpet.data.local.EventDao
import ru.mirea.guseva.fitpet.data.model.Event

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    suspend fun update(event: Event) {
        eventDao.update(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }
}

