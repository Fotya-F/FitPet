package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int? = null,
    val animalName: String? = null,
    val name: String? = null,
    val description: String,
    val date: String? = null,
    val eventTime: Long? = null,
    val imageUrl: String? = null,
    val userId: String
)