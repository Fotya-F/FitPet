package ru.mirea.guseva.fitpet.data.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.ArticleAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        setupRecyclerView()
        setupSearchView()
        setupFilterButton()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewArticles.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewArticles.adapter = ArticleAdapter(emptyList()) { article ->
            // Handle item click here, open article detail
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { feedViewModel.filterArticles(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { feedViewModel.filterArticles(it) }
                return false
            }
        })
    }

    private fun setupFilterButton() {
        binding.filterButton.setOnClickListener {
            // Handle filter logic here
        }
    }

    private fun observeData() {
        feedViewModel.filteredArticles.observe(viewLifecycleOwner) { articles ->
            (binding.recyclerViewArticles.adapter as ArticleAdapter).updateData(articles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
