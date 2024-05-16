package ru.mirea.guseva.fitpet.repository

import ru.mirea.guseva.fitpet.database.dao.PetDao
import ru.mirea.guseva.fitpet.database.entities.PetEntity
import ru.mirea.guseva.fitpet.model.PetProfile

class PetRepository(private val petDao: PetDao) {
    fun getAllPets(): List<PetProfile> {
        return petDao.getAllPets().map {
            PetProfile(it.id, it.name, it.type, it.age)
        }
    }

    fun addPet(pet: PetProfile) {
        petDao.insertPet(PetEntity(pet.id, pet.name, pet.type, pet.age))
    }
}
