package ru.mirea.guseva.fitpet.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.mirea.guseva.fitpet.data.local.entities.Article
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import ru.mirea.guseva.fitpet.utils.Converters

@Database(entities = [Article::class, Event::class, Pet::class, SmartDevice::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun eventDao(): EventDao
    abstract fun petDao(): PetDao
    abstract fun deviceDao(): DeviceDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                runBlocking {
                    populateDatabase(database.articleDao())
                }
            }
        }

        suspend fun populateDatabase(articleDao: ArticleDao) {
            articleDao.deleteAll()
            val articles = listOf(
                Article(title = "Уход за собаками", content = "Собаки - отличные компаньоны...", imageUrl = "url1", tags = listOf("Собака")),
                Article(title = "Уход за кошками", content = "Кошки - независимые животные...", imageUrl = "url2", tags = listOf("Кошка")),
                Article(title = "Уход за черепахами", content = "Черепахи требуют сбалансированного питания...", imageUrl = "url3", tags = listOf("Черепаха"))
            )
            articles.forEach { article -> articleDao.insertArticle(article) }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitpet_db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
