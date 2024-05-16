package ru.mirea.guseva.fitpet.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.mirea.guseva.fitpet.database.entities.PetEntity

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): List<PetEntity>

    @Insert
    fun insertPet(pet: PetEntity)

    @Delete
    fun deletePet(pet: PetEntity)
}
