package ru.mirea.guseva.fitpet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.EventDao
import ru.mirea.guseva.fitpet.data.local.entities.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("events")

    fun getEventsByPet(petId: Int): Flow<List<Event>> {
        return eventDao.getEventsByPet(petId)
    }

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
        val firestoreEvent = event.copy(userId = FirebaseAuth.getInstance().currentUser?.uid)
        collection.add(firestoreEvent).await()
    }

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreEvents = collection.whereEqualTo("userId", userId).get().await().toObjects(Event::class.java)
        firestoreEvents.forEach { eventDao.insertEvent(it) }
    }
}
