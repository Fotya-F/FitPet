package ru.mirea.guseva.fitpet

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.ArticleRepository
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.local.ArticleDao
import ru.mirea.guseva.fitpet.data.local.entities.Article

@HiltAndroidApp
class FitPetApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = AppDatabase.getDatabase(applicationContext, applicationScope)
        applicationScope.launch {
            val articleRepository = ArticleRepository(database.articleDao())
            if (articleRepository.isArticleTableEmpty()) {
                populateDatabase(database.articleDao())
            }
        }
    }

    private suspend fun populateDatabase(articleDao: ArticleDao) {
        val articles = listOf(
            Article(title = "Уход за собаками", content = "Собаки - отличные компаньоны. Они требуют регулярного ухода и внимания.", imageUrl = "url1", tags = listOf("Собака")),
            Article(title = "Уход за кошками", content = "Кошки - независимые животные, но также нуждаются в заботе и уходе.", imageUrl = "url2", tags = listOf("Кошка")),
            Article(title = "Уход за черепахами", content = "Черепахи требуют сбалансированного питания и правильных условий содержания.", imageUrl = "url3", tags = listOf("Черепаха"))
        )
        articles.forEach { article ->
            articleDao.insertArticle(article)
        }
    }
}