package ru.mirea.guseva.fitpet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.ArticleRepository
import ru.mirea.guseva.fitpet.data.local.entities.Article
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _filteredArticles = MutableLiveData<List<Article>>()
    val filteredArticles: LiveData<List<Article>> = _filteredArticles

    private val _allArticles = MutableLiveData<List<Article>>()
    private val allArticles: LiveData<List<Article>> = _allArticles

    private val _selectedTags = MutableLiveData<List<String>>()
    val selectedTags: LiveData<List<String>> = _selectedTags

    var isAutoFilterEnabled = true
    private var isFavoriteFilterActive = false

    init {
        loadArticles()
        _selectedTags.value = emptyList()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            try {
                articleRepository.getAllArticles().collect { articles ->
                    _allArticles.value = articles
                    _filteredArticles.value = articles
                    Log.d("FeedViewModel", "Loaded articles: ${articles.size}")
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error loading articles", e)
            }
        }
    }

    fun refreshArticles() {
        loadArticles()
    }

    fun searchArticles(query: String) {
        val filteredList = allArticles.value?.filter { it.title.contains(query, ignoreCase = true) }.orEmpty()
        _filteredArticles.value = filteredList
    }

    fun filterArticlesByPets(pets: List<Pet>) {
        if (isAutoFilterEnabled) {
            val petTypes = pets.map { it.type }
            _filteredArticles.value = _allArticles.value?.filter { article ->
                petTypes.any { type -> article.title.contains(type, ignoreCase = true) }
            }.orEmpty()
        }
    }

    fun filterArticlesByTags(tags: List<String>) {
        _selectedTags.value = tags
        if (tags.isEmpty()) {
            _filteredArticles.value = _allArticles.value.orEmpty()
        } else {
            _filteredArticles.value = _allArticles.value?.filter { article ->
                tags.any { tag -> article.tags.any { it.equals(tag, ignoreCase = true) } }
            }.orEmpty()
        }
    }

    fun toggleFavoriteFilter() {
        isFavoriteFilterActive = !isFavoriteFilterActive
        if (isFavoriteFilterActive) {
            viewModelScope.launch {
                articleRepository.getFavoriteArticles().collect { favoriteArticles ->
                    _filteredArticles.value = favoriteArticles
                }
            }
        } else {
            _filteredArticles.value = _allArticles.value.orEmpty()
        }
    }

    fun syncAndLoadArticles() {
        viewModelScope.launch {
            try {
                // articleRepository.syncWithFirestore()
                loadArticles()
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error syncing articles: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun addArticle(article: Article) {
        viewModelScope.launch {
            try {
                articleRepository.insertArticle(article)
                // Загрузка статей только при необходимости
                // syncAndLoadArticles()
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error adding article: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun clearAndLoadArticles() {
        viewModelScope.launch {
            try {
                // articleRepository.clearFirestore()
                // addSampleArticles() // Убедитесь, что этот метод не вызывается
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error clearing Firestore: ${e.message}")
            }
        }
    }

    // private fun addSampleArticles() {
    //     viewModelScope.launch {
    //         val articles = listOf(
    //             Article(title = "Уход за харьками", content = "Харьки - активные животные, требующие большого количества внимания и ухода.", imageUrl = "url4", tags = listOf("Харьки")),
    //             Article(title = "Уход за крысами", content = "Крысы - умные и социальные животные, которые могут стать отличными домашними питомцами.", imageUrl = "url5", tags = listOf("Крысы")),
    //             Article(title = "Уход за обезьянами", content = "Обезьяны - очень умные и требовательные животные, нуждающиеся в особом уходе и условиях содержания.", imageUrl = "url6", tags = listOf("Обезьяны"))
    //         )
    //         articles.forEach { article ->
    //             try {
    //                 articleRepository.insertArticle(article)
    //             } catch (e: Exception) {
    //                 Log.e("FeedViewModel", "Error adding sample article: ${e.message}")
    //             }
    //         }
    //         syncAndLoadArticles()
    //     }
    // }
}
