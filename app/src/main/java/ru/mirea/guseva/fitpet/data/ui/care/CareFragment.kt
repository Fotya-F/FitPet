package ru.mirea.guseva.fitpet.data.ui.care

import android.os.Bundle
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

        setupRecyclerView()
        setupRecommendationsButton()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewPets.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPets.adapter = PetAdapter(emptyList()) { pet ->
            // Handle item click here
        }
    }

    private fun setupRecommendationsButton() {
        binding.buttonRecommendations.setOnClickListener {
            // Показать рекомендации
        }
    }

    private fun observeData() {
        careViewModel.allPets.observe(viewLifecycleOwner) { pets ->
            (binding.recyclerViewPets.adapter as PetAdapter).updateData(pets)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
