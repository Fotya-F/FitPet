package ru.mirea.guseva.fitpet.data.model

data class Status(
    val id: Int,
    val sleep: String,
    val sleepStatus: String,
    val weight: String,
    val weightStatus: String,
    val activity: String,
    val activityStatus: String
)
