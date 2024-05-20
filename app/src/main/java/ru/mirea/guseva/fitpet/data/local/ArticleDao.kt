package ru.mirea.guseva.fitpet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.mirea.guseva.fitpet.data.model.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)
}
