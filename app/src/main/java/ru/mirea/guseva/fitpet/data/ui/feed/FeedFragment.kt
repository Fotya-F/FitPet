package ru.mirea.guseva.fitpet.data.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.databinding.FragmentFeedBinding
import ru.mirea.guseva.fitpet.data.ui.adapters.ArticleAdapter

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FeedFragment"

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

        val recyclerView = binding.recyclerViewArticles
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        feedViewModel.allArticles.observe(viewLifecycleOwner, { articles ->
            Log.d(TAG, "Articles observed: ${articles.size}")
            recyclerView.adapter = ArticleAdapter(articles) { article ->
                // Handle item click here
            }
            Log.d(TAG, "Adapter set with ${articles.size} items")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
