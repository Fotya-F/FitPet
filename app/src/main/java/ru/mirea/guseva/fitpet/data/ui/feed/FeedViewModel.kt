package ru.mirea.guseva.fitpet.data.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Article
import ru.mirea.guseva.fitpet.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    private val articleDao = AppDatabase.getDatabase(application).articleDao()
    private val repository: ArticleRepository = ArticleRepository(articleDao)
    val allArticles: LiveData<List<Article>> = repository.allArticles

    fun insert(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun update(article: Article) = viewModelScope.launch {
        repository.update(article)
    }

    fun delete(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }
}
