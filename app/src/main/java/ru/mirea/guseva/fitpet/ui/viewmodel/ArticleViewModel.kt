package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.ArticleRepository
import ru.mirea.guseva.fitpet.data.local.entities.Article
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    fun getArticleById(articleId: Int): LiveData<Article?> {
        return articleRepository.getArticleById(articleId).asLiveData()
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            val updatedArticle = article.copy(isFavorite = !article.isFavorite)
            articleRepository.updateArticle(updatedArticle)
        }
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            articleRepository.syncWithFirestore()
        }
    }
}
