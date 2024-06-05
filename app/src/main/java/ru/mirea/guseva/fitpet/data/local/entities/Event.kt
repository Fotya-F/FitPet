package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int? = null, // Для I проекта
    val animalName: String? = null, // Для II проекта
    val name: String? = null, // Для I проекта
    val description: String,
    val date: String? = null, // Для I проекта
    val eventTime: Long? = null, // Для II проекта
    val imageUrl: String? = null, // Для II проекта
    val userId: String? = null // Для Firestore (I проект)
)
