package ru.mirea.guseva.fitpet.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.local.ArticleDao
import ru.mirea.guseva.fitpet.data.local.DeviceDao
import ru.mirea.guseva.fitpet.data.local.EventDao
import ru.mirea.guseva.fitpet.data.local.PetDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val applicationScope = CoroutineScope(SupervisorJob())
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "fitpet_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase): ArticleDao {
        return appDatabase.articleDao()
    }

    @Provides
    fun provideDeviceDao(appDatabase: AppDatabase): DeviceDao {
        return appDatabase.deviceDao()
    }

    @Provides
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    fun providePetDao(appDatabase: AppDatabase): PetDao {
        return appDatabase.petDao()
    }
}
