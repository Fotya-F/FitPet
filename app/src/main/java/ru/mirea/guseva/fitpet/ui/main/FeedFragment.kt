package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.data.local.entities.Article
import ru.mirea.guseva.fitpet.databinding.FragmentFeedBinding
import ru.mirea.guseva.fitpet.ui.adapters.ArticleAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.FeedViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val feedViewModel: FeedViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleAdapter = ArticleAdapter(emptyList()) { article ->
            onArticleClicked(article)
        }
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
        observeViewModel()
        binding.searchButton.setOnClickListener { showSearchDialog() }
        binding.filterButton.setOnClickListener { showFilterDialog() }
        binding.favoriteButton.setOnClickListener { feedViewModel.toggleFavoriteFilter() }
    }

    override fun onResume() {
        super.onResume()
        feedViewModel.refreshArticles()
    }

    private fun observeViewModel() {
        feedViewModel.filteredArticles.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                Log.d("FeedFragment", "Updating articles adapter with ${it.size} articles")
                articleAdapter.updateData(it)
            }
        })
        petViewModel.pets.observe(viewLifecycleOwner, Observer { pets ->
            if (feedViewModel.isAutoFilterEnabled) {
                feedViewModel.filterArticlesByPets(pets)
            }
        })
    }

    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Search Articles")
        val input = EditText(requireContext())
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                feedViewModel.searchArticles(s.toString())
            }
        })
        builder.setView(input)
        builder.setPositiveButton("Search") { _, _ ->
            val query = input.text.toString()
            feedViewModel.searchArticles(query)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showFilterDialog() {
        val petTypes = resources.getStringArray(R.array.pet_types)
        val selectedTags = feedViewModel.selectedTags.value ?: emptyList()
        val selectedItems = petTypes.map { selectedTags.contains(it) }.toBooleanArray()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Filter Articles")
        builder.setMultiChoiceItems(petTypes, selectedItems) { _, which, isChecked ->
            selectedItems[which] = isChecked
        }
        builder.setPositiveButton("Apply") { _, _ ->
            val selectedPetTypes = petTypes.filterIndexed { index, _ -> selectedItems[index] }
            feedViewModel.filterArticlesByTags(selectedPetTypes)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.setNeutralButton("Clear Filters") { _, _ ->
            feedViewModel.filterArticlesByTags(emptyList())
        }
        builder.show()
    }

    private fun onArticleClicked(article: Article) {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToArticleDetailFragment(article.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
