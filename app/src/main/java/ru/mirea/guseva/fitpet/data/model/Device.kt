package ru.mirea.guseva.fitpet.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val petId: Int,  // Foreign key referencing Pet
    val connectionStatus: Boolean
)