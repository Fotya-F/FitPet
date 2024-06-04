package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.databinding.FragmentHomeBinding
import ru.mirea.guseva.fitpet.ui.adapters.PetCardAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var petCardAdapter: PetCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petCardAdapter = PetCardAdapter(emptyList()) { pet -> onPetSelected(pet) }

        binding.rvPets.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = petCardAdapter
        }

        homeViewModel.pets.observe(viewLifecycleOwner, Observer { pets ->
            petCardAdapter.updateData(pets)
        })
    }

    private fun onPetSelected(pet: Pet) {
        binding.petInfoContainer.visibility = View.VISIBLE
        binding.nextAppointment.text = "Ближайшая дата приема: ${pet.lastVetVisit}"
        binding.petStatus.text = "Состояние питомца: сон, вес, активность..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
