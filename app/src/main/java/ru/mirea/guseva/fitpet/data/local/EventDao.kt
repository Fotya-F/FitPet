package ru.mirea.guseva.fitpet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.entities.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<Event?>

    @Update
    suspend fun update(event: Event)
    @Query("SELECT * FROM event")
    fun getAllEvents(): Flow<List<Event>>
    @Query("SELECT * FROM event WHERE petId = :petId AND userId = :userId")
    fun getEventsByPetAndUser(petId: Int, userId: String): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE userId = :userId")
    fun getAllEventsByUser(userId: String): Flow<List<Event>>
    @Query("SELECT * FROM event WHERE petId = :petId")
    fun getEventsByPet(petId: Int): Flow<List<Event>>


    @Query("SELECT * FROM event")
    fun getAllLiveDataEvents(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("DELETE FROM event WHERE eventTime < :currentTime")
    suspend fun deleteOldEvents(currentTime: Long)
}
