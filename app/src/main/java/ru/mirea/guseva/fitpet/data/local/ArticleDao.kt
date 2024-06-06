package ru.mirea.guseva.fitpet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.entities.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    fun getArticleById(articleId: Int): Flow<Article?>

    @Query("SELECT * FROM articles WHERE isFavorite = 1")
    fun getFavoriteArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE :tag IN (tags)")
    fun getArticlesByTag(tag: String): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticleCount(): Int

}
