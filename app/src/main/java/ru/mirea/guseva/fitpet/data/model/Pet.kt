package ru.mirea.guseva.fitpet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.mirea.guseva.fitpet.data.Converters

@Entity(tableName = "pets")
@TypeConverters(Converters::class)
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val species: String,
    val breed: String,
    val avatarUrl: String?,
    val status: String,  // добавлено состояние здоровья питомца
    val state: String,   // добавлено общее состояние (например, красный, желтый, зеленый)
    val plans: List<String>,  // добавлен список планов
    val checks: List<String>  // добавлен список проверок
)
