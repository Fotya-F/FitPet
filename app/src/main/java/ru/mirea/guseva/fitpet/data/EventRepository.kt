package ru.mirea.guseva.fitpet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.EventDao
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {
    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    fun getEventsByPetAndUser(petId: Int, userId: String): Flow<List<Event>> {
        return eventDao.getEventsByPetAndUser(petId, userId)
    }

    fun getAllEventsByUser(userId: String): Flow<List<Event>> {
        return eventDao.getAllEventsByUser(userId)
    }

    fun getAllLiveDataEvents(): LiveData<List<Event>> {
        return Transformations.map(eventDao.getAllLiveDataEvents()) { events ->
            events.filter { it.eventTime ?: Long.MAX_VALUE > System.currentTimeMillis() }
        }
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insert(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }

    suspend fun deleteOldEvents() {
        eventDao.deleteOldEvents(System.currentTimeMillis())
    }
}
