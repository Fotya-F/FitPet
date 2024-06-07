package ru.mirea.guseva.fitpet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.PetDao
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(private val petDao: PetDao) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("pets")

    fun getAllPetsByUser(userId: String): Flow<List<Pet>> = petDao.getAllPetsByUser(userId)

    suspend fun insertPet(pet: Pet) {
        petDao.insertPet(pet)
    }

    val allPets: Flow<List<Pet>> = petDao.getAllPets()


    suspend fun updatePet(pet: Pet) {
        petDao.updatePet(pet)
        // collection.document(pet.id.toString()).set(pet).await()
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
        // collection.document(pet.id.toString()).delete().await()
    }

    fun getPetById(petId: Int): Flow<Pet?> {
        return petDao.getPetById(petId)
    }

    suspend fun getPetByName(petName: String): Pet? {
        return petDao.getPetByName(petName)
    }

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestorePets = petDao.getAllPetsByUser(userId).firstOrNull()?.let { pets ->
            pets.map { it.copy(userId = userId) }
        } ?: emptyList()
        firestorePets.forEach { pet ->
            val petRef = collection.document(pet.id.toString())
            petRef.set(pet).await()
        }
    }

    suspend fun restoreFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestorePets = collection.whereEqualTo("userId", userId).get().await().toObjects(Pet::class.java)
        firestorePets.forEach { pet ->
            petDao.insertPet(pet)
        }
    }
}
