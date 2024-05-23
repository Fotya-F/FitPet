package ru.mirea.guseva.fitpet.data.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.guseva.fitpet.data.model.Pet

class MainViewModel : ViewModel() {
    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    init {
        // Sample data
        _pets.value = listOf(
            Pet(
                id = 1,
                name = "Тоша",
                gender = "Мужской",
                age = 5,
                weight = 12.5,
                species = "Кот",
                breed = "Мейн-кун",
                avatarUrl = null,
                status = "Плохое",
                state = "Красный",
                plans = listOf("Прогулка в парке", "Визит к ветеринару"),
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
                avatarUrl = null,
                status = "Хорошее",
                state = "Зеленый",
                plans = listOf("Проверка зубов"),
                checks = listOf("Сон", "Вес")
            ),
            Pet(
                id = 3,
                name = "Фрося",
                gender = "Женский",
                age = 4,
                weight = 9.0,
                species = "Кот",
                breed = "Персидская",
                avatarUrl = null,
                status = "Нормальное",
                state = "Желтый",
                plans = listOf("Покупка новой игрушки"),
                checks = listOf("Сон", "Активность")
            )
        )
    }
}
