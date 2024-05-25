package ru.mirea.guseva.fitpet.data.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.guseva.fitpet.data.model.Pet
import ru.mirea.guseva.fitpet.data.model.Status

class MainViewModel : ViewModel() {

    private val _pets = MutableLiveData<List<Pet>>().apply {
        value = listOf(
            Pet(
                id = 1,
                name = "Тоша",
                gender = "Мужской",
                age = 5,
                weight = 12.5,
                species = "Кот",
                breed = "Мейн-кун",
                avatarUrl = "https://example.com/toha.jpg",
                status = "Плохое",
                state = "Красный",
                plans = listOf("8 мая", "Прогулка в парке", "Визит к ветеринару"),
                checks = listOf("Сон", "Вес", "Активность")
            ),
            Pet(
                id = 2,
                name = "Бася",
                gender = "Женский",
                age = 3,
                weight = 8.2,
                species = "Собака",
                breed = "Лабрадор",
                avatarUrl = "https://example.com/basya.jpg",
                status = "Хорошее",
                state = "Зеленый",
                plans = listOf("10 мая", "Проверка зубов"),
                checks = listOf("Сон", "Вес")
            )
        )
    }
    val pets: LiveData<List<Pet>> = _pets

    private val _selectedPet = MutableLiveData<Pet?>()
    val selectedPet: LiveData<Pet?> = _selectedPet

    private val _status = MutableLiveData<List<Status>>()
    val status: LiveData<List<Status>> = _status

    fun selectPet(pet: Pet) {
        _selectedPet.value = pet
        updateStatus(pet)
    }

    fun updateStatus(pet: Pet) {
        _status.value = listOf(
            Status(
                id = 1,
                sleep = "8 часов",
                sleepStatus = "в норме",
                weight = "${pet.weight} кг",
                weightStatus = "в норме",
                activity = "12 часов",
                activityStatus = "стоит обратить внимание"
            )
        )
    }
}
