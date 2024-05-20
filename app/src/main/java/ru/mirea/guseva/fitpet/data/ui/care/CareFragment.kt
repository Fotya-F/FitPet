package ru.mirea.guseva.fitpet.data.ui.care

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentCareBinding

class CareFragment : Fragment() {

    private lateinit var careViewModel: CareViewModel
    private var _binding: FragmentCareBinding? = null
    private val binding get() = _binding!!
    private val TAG = "CareFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        careViewModel = ViewModelProvider(this).get(CareViewModel::class.java)

        val recyclerView = binding.recyclerViewPets
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        careViewModel.allPets.observe(viewLifecycleOwner, { pets ->
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
