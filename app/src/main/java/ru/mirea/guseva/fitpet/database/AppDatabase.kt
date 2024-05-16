package ru.mirea.guseva.fitpet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mirea.guseva.fitpet.database.dao.PetDao
import ru.mirea.guseva.fitpet.database.dao.UserDao
import ru.mirea.guseva.fitpet.database.entities.PetEntity
import ru.mirea.guseva.fitpet.database.entities.UserEntity

@Database(entities = [PetEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun userDao(): UserDao
}
