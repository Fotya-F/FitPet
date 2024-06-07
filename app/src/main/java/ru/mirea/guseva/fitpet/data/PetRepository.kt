package ru.mirea.guseva.fitpet.data

import android.content.SharedPreferences
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
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val sharedPreferences: SharedPreferences
) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("pets")

    fun getAllPetsByUser(userId: String): Flow<List<Pet>> = petDao.getAllPetsByUser(userId)

    suspend fun insertPet(pet: Pet) {
        petDao.insertPet(pet)
        generateInitialStatistics(pet.id)
    }

    val allPets: Flow<List<Pet>> = petDao.getAllPets()

    suspend fun updatePet(pet: Pet) {
        petDao.updatePet(pet)
        collection.document(pet.id.toString()).set(pet).await()
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
        collection.document(pet.id.toString()).delete().await()
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

    private fun generateInitialStatistics(petId: Int) {
        val sleepValue = (5..10).random() * 10f
        val activityValue = (5..10).random() * 10f
        val weightValue = (5..10).random() * 10f
        saveStatistics(petId, sleepValue, activityValue, weightValue)
    }

    private fun saveStatistics(petId: Int, sleepValue: Float, activityValue: Float, weightValue: Float) {
        sharedPreferences.edit().apply {
            putFloat("sleep_$petId", sleepValue)
            putFloat("activity_$petId", activityValue)
            putFloat("weight_$petId", weightValue)
            putLong("last_update_$petId", System.currentTimeMillis())
            apply()
        }
    }

    fun getSleepValue(petId: Int): Float {
        return sharedPreferences.getFloat("sleep_$petId", 55f)
    }

    fun getActivityValue(petId: Int): Float {
        return sharedPreferences.getFloat("activity_$petId", 25f)
    }

    fun getWeightValue(petId: Int): Float {
        return sharedPreferences.getFloat("weight_$petId", 15f)
    }

    fun getLastUpdate(petId: Int): Long {
        return sharedPreferences.getLong("last_update_$petId", 0L)
    }

    fun updateStatisticsIfNeeded(petId: Int) {
        val lastUpdate = getLastUpdate(petId)
        val currentTime = System.currentTimeMillis()
        val sixHoursInMillis = 6 * 60 * 60 * 1000
        if (currentTime - lastUpdate >= sixHoursInMillis) {
            val sleepValue = getUpdatedValue(getSleepValue(petId))
            val activityValue = getUpdatedValue(getActivityValue(petId))
            val weightValue = getUpdatedValue(getWeightValue(petId))
            saveStatistics(petId, sleepValue, activityValue, weightValue)
        }
    }

    private fun getUpdatedValue(currentValue: Float): Float {
        val change = (0..10).random() - 5 // Random change between -5 and 5
        return (currentValue + change).coerceIn(0f, 100f) // Ensure value stays within 0-100 range
    }
}