package ru.mirea.guseva.fitpet.data.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter
import ru.mirea.guseva.fitpet.data.ui.adapters.PlanAdapter
import ru.mirea.guseva.fitpet.data.ui.adapters.StatusAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

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

        setupRecyclerViews()
        observeData()
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewPets.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPets.adapter = PetAdapter(emptyList()) { pet ->
            // Handle item click here
        }

        binding.recyclerViewPlans.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPlans.adapter = PlanAdapter(emptyList())

        binding.recyclerViewStatus.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewStatus.adapter = StatusAdapter(emptyList())
    }

    private fun observeData() {
        mainViewModel.pets.observe(viewLifecycleOwner) { pets ->
            (binding.recyclerViewPets.adapter as PetAdapter).updateData(pets)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
