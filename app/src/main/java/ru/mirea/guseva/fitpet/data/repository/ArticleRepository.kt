package ru.mirea.guseva.fitpet.data.repository

import androidx.lifecycle.LiveData
import ru.mirea.guseva.fitpet.data.local.ArticleDao
import ru.mirea.guseva.fitpet.data.model.Article

class ArticleRepository(private val articleDao: ArticleDao) {

    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }

    suspend fun update(article: Article) {
        articleDao.update(article)
    }

    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}
