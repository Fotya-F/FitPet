package ru.mirea.guseva.fitpet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mirea.guseva.fitpet.data.Converters
import ru.mirea.guseva.fitpet.data.model.Article
import ru.mirea.guseva.fitpet.data.model.Device
import ru.mirea.guseva.fitpet.data.model.Event
import ru.mirea.guseva.fitpet.data.model.Pet

@Database(entities = [Pet::class, Event::class, Article::class, Device::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun eventDao(): EventDao
    abstract fun articleDao(): ArticleDao
    abstract fun deviceDao(): DeviceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitpet_database"
                ).fallbackToDestructiveMigration() // Добавьте это, если не хотите писать миграции вручную
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

