package ru.mirea.guseva.fitpet.data.repository

import androidx.lifecycle.LiveData
import ru.mirea.guseva.fitpet.data.local.PetDao
import ru.mirea.guseva.fitpet.data.model.Pet

class PetRepository(private val petDao: PetDao) {

    val allPets: LiveData<List<Pet>> = petDao.getAllPets()

    suspend fun insert(pet: Pet) {
        petDao.insert(pet)
    }

    suspend fun update(pet: Pet) {
        petDao.update(pet)
    }

    suspend fun delete(pet: Pet) {
        petDao.delete(pet)
    }
}
