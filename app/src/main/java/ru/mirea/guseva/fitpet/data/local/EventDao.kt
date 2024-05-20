package ru.mirea.guseva.fitpet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.mirea.guseva.fitpet.data.model.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)
}