package ru.mirea.guseva.fitpet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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

    fun getAllArticles(): Flow<List<Article>> = articleDao.getAllArticles()

    fun getArticleById(articleId: Int): Flow<Article?> = articleDao.getArticleById(articleId)

    fun getFavoriteArticles(): Flow<List<Article>> = articleDao.getFavoriteArticles()

    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
        val firestoreArticle = article.copy(userId = FirebaseAuth.getInstance().currentUser?.uid)
        collection.add(firestoreArticle).await()
    }

    suspend fun updateArticle(article: Article) {
        articleDao.updateArticle(article)
        collection.document(article.id.toString()).set(article).await()
    }

    suspend fun toggleFavorite(articleId: Int) {
        val article = articleDao.getArticleById(articleId).firstOrNull() ?: return
        val updatedArticle = article.copy(isFavorite = !article.isFavorite)
        updateArticle(updatedArticle)
    }

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreArticles = collection.whereEqualTo("userId", userId).get().await().toObjects(Article::class.java)
        firestoreArticles.forEach { articleDao.insertArticle(it) }
    }
}
