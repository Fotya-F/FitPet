package ru.mirea.guseva.fitpet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.entities.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE petId = :petId")
    fun getEventsByPet(petId: Int): Flow<List<Event>>

    @Query("SELECT * FROM event")
    fun getAllEvents(): Flow<List<Event>>

    @Insert
    suspend fun insertEvent(event: Event)
}
