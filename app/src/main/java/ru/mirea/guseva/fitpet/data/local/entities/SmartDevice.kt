package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_devices")
data class SmartDevice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val petId: Int,
    val type: String,
    val isConnected: Boolean,
    val userId: String? = null // Для Firestore
)
