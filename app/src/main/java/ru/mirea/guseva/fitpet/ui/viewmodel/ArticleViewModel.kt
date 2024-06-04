package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.ArticleRepository
import ru.mirea.guseva.fitpet.data.local.entities.Article
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    fun getArticleById(articleId: Int): LiveData<Article?> {
        return repository.getArticleById(articleId).asLiveData()
    }

    fun toggleFavorite(articleId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(articleId)
        }
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            repository.syncWithFirestore()
        }
    }

}
