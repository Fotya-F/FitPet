package ru.mirea.guseva.fitpet.data.local

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.local.entities.Article
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import ru.mirea.guseva.fitpet.utils.Converters

@Database(entities = [Article::class, Event::class, Pet::class, SmartDevice::class], version = 8, exportSchema = false)
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
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(database.articleDao())
//                }
//            }
        }
        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE pets ADD COLUMN userId TEXT")
                database.execSQL("ALTER TABLE event ADD COLUMN userId TEXT")
                database.execSQL("ALTER TABLE smart_devices ADD COLUMN userId TEXT")
            }
        }

        suspend fun populateDatabase(articleDao: ArticleDao) {
            Log.d("AppDatabase", "Populating database with sample articles")
            articleDao.deleteAll()
            val articles = listOf(
                Article(title = "Уход за собаками", content = "Собаки - отличные компаньоны. Они требуют регулярного ухода и внимания.", imageUrl = "url1", tags = listOf("Собака")),
                Article(title = "Уход за кошками", content = "Кошки - независимые животные, но также нуждаются в заботе и уходе.", imageUrl = "url2", tags = listOf("Кошка")),
                Article(title = "Уход за черепахами", content = "Черепахи требуют сбалансированного питания и правильных условий содержания.", imageUrl = "url3", tags = listOf("Черепаха"))
            )
            articles.forEach { article ->
                articleDao.insertArticle(article)
                Log.d("AppDatabase", "Inserted article: ${article.title}")
            }
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
                ).addCallback(AppDatabaseCallback(scope)).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
