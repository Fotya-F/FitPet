package ru.mirea.guseva.fitpet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val species: String,
    val breed: String,
    val avatarUrl: String?
)