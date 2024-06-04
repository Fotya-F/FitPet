package ru.mirea.guseva.fitpet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.entities.Pet

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: Int): Flow<Pet?>

    @Query("SELECT * FROM pets WHERE name = :petName LIMIT 1")
    suspend fun getPetByName(petName: String): Pet?

    @Insert
    suspend fun insertPet(pet: Pet)

    @Update
    suspend fun updatePet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)
}
