package ru.mirea.guseva.fitpet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.mirea.guseva.fitpet.data.model.Pet

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): LiveData<List<Pet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: Pet)

    @Update
    suspend fun update(pet: Pet)

    @Delete
    suspend fun delete(pet: Pet)
}
