package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentArticleDetailBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.ArticleViewModel

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val articleViewModel: ArticleViewModel by viewModels()
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

        // Handle back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

        articleViewModel.getArticleById(args.articleId).observe(viewLifecycleOwner, Observer { article ->
            article?.let {
                binding.articleTitle.text = it.title
                binding.articleContent.text = it.content
                binding.favoriteButton.isChecked = it.isFavorite
                binding.favoriteButton.setOnClickListener {
                    articleViewModel.toggleFavorite(article)
                    Toast.makeText(requireContext(), if (article.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
                // Load image using an image loading library like Glide or Picasso
                // Glide.with(this).load(it.imageUrl).into(binding.articleImage)
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
