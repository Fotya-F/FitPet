package ru.mirea.guseva.fitpet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.EventDao
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("events")

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    fun getEventById(eventId: Int): Flow<Event?> {
        return eventDao.getEventById(eventId)
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insert(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }

    suspend fun deleteOldEvents() {
        eventDao.deleteOldEvents(System.currentTimeMillis())
    }

    fun getEventsByPet(petId: Int): Flow<List<Event>> {
        return eventDao.getEventsByPet(petId)
    }
    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreEvents = eventDao.getAllEventsByUser(userId).firstOrNull()?.let { events ->
            events.map { it.copy(userId = userId) }
        } ?: emptyList()
        firestoreEvents.forEach { event ->
            val eventRef = collection.document(event.id.toString())
            eventRef.set(event).await()
        }
    }

    suspend fun restoreFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreEvents = collection.whereEqualTo("userId", userId).get().await().toObjects(Event::class.java)
        firestoreEvents.forEach { event ->
            eventDao.insert(event)
        }
    }
}
