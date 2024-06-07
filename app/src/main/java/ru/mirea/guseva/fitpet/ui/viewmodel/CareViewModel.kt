package ru.mirea.guseva.fitpet.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.PetRepository
import javax.inject.Inject

@HiltViewModel
class CareViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    val pets = petRepository.allPets.asLiveData()

    init {
        //checkAndUpdateStatistics()
    }

    private fun checkAndUpdateStatistics() {
        val lastUpdate = sharedPreferences.getLong("last_update", 0L)
        val currentTime = System.currentTimeMillis()
        if (isTimeToUpdate(lastUpdate, currentTime)) {
            //updateStatistics()
            sharedPreferences.edit().putLong("last_update", currentTime).apply()
        }
    }
    fun updateStatisticsIfNeeded(petId: Int) {
        val lastStatUpdate = sharedPreferences.getLong("stat_update_$petId", 0L)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastStatUpdate >= 6 * 60 * 60 * 1000) {
            viewModelScope.launch {
                val pet = petRepository.getPetById(petId).firstOrNull() ?: return@launch
                val sleepValue = getUpdatedValue(getSleepValue(petId))
                val activityValue = getUpdatedValue(getActivityValue(petId))
                val weightValue = getUpdatedValue(getWeightValue(petId))
                saveStatistics(petId, sleepValue, activityValue, weightValue)
                sharedPreferences.edit().putLong("stat_update_$petId", currentTime).apply()
            }
        }
    }

    private fun isTimeToUpdate(lastUpdate: Long, currentTime: Long): Boolean {
        val sixHoursInMillis = 6 * 60 * 60 * 1000
        return currentTime - lastUpdate >= sixHoursInMillis
    }

    private fun updateStatistics() {
        viewModelScope.launch {
            val pets = petRepository.allPets.firstOrNull() ?: return@launch
            pets.forEach { pet ->
                val sleepValue = getUpdatedValue(getSleepValue(pet.id))
                val activityValue = getUpdatedValue(getActivityValue(pet.id))
                val weightValue = getUpdatedValue(getWeightValue(pet.id))
                saveStatistics(pet.id, sleepValue, activityValue, weightValue)
            }
        }
    }

    private fun getUpdatedValue(currentValue: Float): Float {
        val change = (0..10).random() - 5 // Random change between -5 and 5
        return (currentValue + change).coerceIn(0f, 100f) // Ensure value stays within 0-100 range
    }

    private fun saveStatistics(petId: Int, sleepValue: Float, activityValue: Float, weightValue: Float) {
        sharedPreferences.edit().apply {
            putFloat("sleep_$petId", sleepValue)
            putFloat("activity_$petId", activityValue)
            putFloat("weight_$petId", weightValue)
            apply()
        }
    }

    fun getSleepValue(petId: Int): Float {
        return sharedPreferences.getFloat("sleep_$petId", generateInitialValue())
    }

    fun getActivityValue(petId: Int): Float {
        return sharedPreferences.getFloat("activity_$petId", generateInitialValue())
    }

    fun getWeightValue(petId: Int): Float {
        return sharedPreferences.getFloat("weight_$petId", generateInitialValue())
    }

    private fun generateInitialValue(): Float {
        return (3..8).random() * 10f
    }

    fun getRecommendations(petId: Int): String {
        return "Рекомендации" // Временное значение для примера
    }

    fun getRecommendationsForLabel(label: String?): String {
        return when (label) {
            "Сон" -> "Рекомендации по сну:"
            "Активность" -> "Рекомендации по активности:"
            "Вес" -> "Рекомендации по весу:"
            else -> "Рекомендации"
        }
    }
}