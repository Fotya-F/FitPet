package ru.mirea.guseva.fitpet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.local.entities.Article
import ru.mirea.guseva.fitpet.databinding.ItemArticleBinding

class ArticleAdapter(
    private var articles: List<Article>,
    private val clickListener: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position], clickListener)
    }

    override fun getItemCount(): Int = articles.size

    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, clickListener: (Article) -> Unit) {
            binding.articleTitle.text = article.title
            binding.articleDescription.text = article.content.take(100) // Show first 100 characters
            binding.root.setOnClickListener { clickListener(article) }
        }
    }
}
