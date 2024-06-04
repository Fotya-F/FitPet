package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.databinding.FragmentArticleDetailBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.ArticleViewModel

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleId = args.articleId
        viewModel.getArticleById(articleId).observe(viewLifecycleOwner, Observer { article ->
            article?.let {
                binding.articleTitle.text = it.title
                binding.articleContent.text = it.content
                binding.favoriteButton.isChecked = it.isFavorite
            }
        })

        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite(articleId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
