package ru.mirea.guseva.fitpet.data.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.databinding.FragmentMainBinding
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val TAG = "MainFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val recyclerView = binding.recyclerViewPets
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        mainViewModel.allPets.observe(viewLifecycleOwner, { pets ->
            Log.d(TAG, "Pets observed: ${pets.size}")
            recyclerView.adapter = PetAdapter(pets) { pet ->
                // Handle item click here
            }
            Log.d(TAG, "Adapter set with ${pets.size} items")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
