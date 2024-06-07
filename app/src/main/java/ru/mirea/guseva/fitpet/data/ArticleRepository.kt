package ru.mirea.guseva.fitpet.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.ArticleDao
import ru.mirea.guseva.fitpet.data.local.entities.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val articleDao: ArticleDao
) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("articles")
    fun getAllArticles(): Flow<List<Article>> {
        return articleDao.getAllArticles().map { articles ->
            Log.d("ArticleRepository", "Retrieved ${articles.size} articles from database")
            articles
        }
    }
    fun getArticleById(articleId: Int): Flow<Article?> = articleDao.getArticleById(articleId)
    fun getFavoriteArticles(): Flow<List<Article>> = articleDao.getFavoriteArticles()

    suspend fun isArticleTableEmpty(): Boolean {
        return articleDao.getArticleCount() == 0
    }

    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
        // val firestoreArticle = article.copy(userId = FirebaseAuth.getInstance().currentUser?.uid)
        // collection.add(firestoreArticle).await()
    }

    suspend fun updateArticle(article: Article) {
        articleDao.updateArticle(article)
        // collection.document(article.id.toString()).set(article).await()
    }

    suspend fun toggleFavorite(articleId: Int) {
        val article = articleDao.getArticleById(articleId).firstOrNull() ?: return
        val updatedArticle = article.copy(isFavorite = !article.isFavorite)
        updateArticle(updatedArticle)
    }

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        try {
            val firestoreArticles = articleDao.getAllArticles().firstOrNull()?.let { articles ->
                articles.map { it.copy(userId = userId) }
            } ?: emptyList()
            firestoreArticles.forEach { article ->
                val articleRef = collection.document(article.id.toString())
                articleRef.set(article).await()
            }
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error syncing articles: ${e.message}")
        }
    }

    suspend fun restoreFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        try {
            val firestoreArticles = collection.whereEqualTo("userId", userId).get().await().toObjects(Article::class.java)
            firestoreArticles.forEach { article ->
                articleDao.insertArticle(article)
            }
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error restoring articles: ${e.message}")
        }
    }

    suspend fun clearFirestore() {
        try {
            // val documents = collection.get().await()
            // for (document in documents) {
            //     collection.document(document.id).delete().await()
            // }
            Log.d("ArticleRepository", "Cleared Firestore articles")
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error clearing Firestore: ${e.message}")
        }
    }

}
