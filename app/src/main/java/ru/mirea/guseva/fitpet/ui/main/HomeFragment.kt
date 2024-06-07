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
import ru.mirea.guseva.fitpet.ui.adapters.EventAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var petCardAdapter: PetCardAdapter
    private lateinit var eventAdapter: EventAdapter

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

        eventAdapter = EventAdapter(emptyList()) { event -> /* Handle event click */ }
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        homeViewModel.pets.observe(viewLifecycleOwner, Observer { pets ->
            petCardAdapter.updateData(pets)
        })

        homeViewModel.events.observe(viewLifecycleOwner, Observer { events ->
            eventAdapter.updateData(events)
        })
    }

    private fun onPetSelected(pet: Pet) {
        // Убираем обновление UI при выборе питомца
        // binding.petInfoContainer.visibility = View.VISIBLE
        // binding.petName.text = pet.name
        // binding.nextAppointment.text = "Ближайшая дата приема: ${pet.lastVetVisit}"
        binding.petSleep.text = "Сон: ${homeViewModel.getSleepValue(pet.id)}"
        binding.petWeight.text = "Вес: ${homeViewModel.getWeightValue(pet.id)}"
        binding.petActivity.text = "Активность: ${homeViewModel.getActivityValue(pet.id)}"

        // Обновляем список событий только для выбранного питомца
        homeViewModel.getEventsByPet(pet.id).observe(viewLifecycleOwner, Observer { events ->
            eventAdapter.updateData(events)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
