package ru.mirea.guseva.fitpet.data.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter
import ru.mirea.guseva.fitpet.data.ui.adapters.StatusAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
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

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupPetRecyclerView()
        setupStatusRecyclerView()

        mainViewModel.selectedPet.observe(viewLifecycleOwner) { pet ->
            pet?.let {
                binding.nextPlanDate.text = it.plans.firstOrNull() ?: "Нет планов"
                // Обновление статуса для выбранного питомца
                mainViewModel.updateStatus(it)
            }
        }
    }

    private fun setupPetRecyclerView() {
        binding.recyclerViewPets.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val petAdapter = PetAdapter(emptyList()) { pet ->
            mainViewModel.selectPet(pet)
        }
        binding.recyclerViewPets.adapter = petAdapter

        mainViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petAdapter.updateData(pets)
        }
    }

    private fun setupStatusRecyclerView() {
        binding.recyclerViewStatus.layoutManager = LinearLayoutManager(context)
        val statusAdapter = StatusAdapter(emptyList())
        binding.recyclerViewStatus.adapter = statusAdapter

        mainViewModel.status.observe(viewLifecycleOwner) { statuses ->
            statusAdapter.updateData(statuses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
