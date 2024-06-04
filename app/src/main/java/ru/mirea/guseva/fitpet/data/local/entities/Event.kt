package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val name: String,
    val description: String,
    val date: String,
    val userId: String? = null // Для Firestore
)
