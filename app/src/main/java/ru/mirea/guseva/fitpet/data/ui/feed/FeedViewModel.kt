package ru.mirea.guseva.fitpet.data.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Article
import ru.mirea.guseva.fitpet.data.repository.ArticleRepository

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    private val articleDao = AppDatabase.getDatabase(application).articleDao()
    private val repository: ArticleRepository = ArticleRepository(articleDao)
    private val allArticles: LiveData<List<Article>> = repository.allArticles

    private val _searchQuery = MutableLiveData<String>("")
    val filteredArticles: LiveData<List<Article>> = _searchQuery.switchMap { query ->
        if (query.isEmpty()) {
            allArticles
        } else {
            allArticles.map { articles ->
                articles.filter { it.title.contains(query, true) || it.content.contains(query, true) }
            }
        }
    }

    fun filterArticles(query: String) {
        _searchQuery.value = query
    }
}
