package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_devices")
data class SmartDevice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val petId: Int = 0,
    val type: String = "",
    val isConnected: Boolean = false,
    val userId: String = ""
) {
    constructor() : this(0, "", 0, "", false, "")
}