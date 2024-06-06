package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentAddPetBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel
import ru.mirea.guseva.fitpet.data.local.entities.Pet

@AndroidEntryPoint
class AddPetFragment : Fragment() {

    private var _binding: FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the spinner with pet types
        val petTypes = resources.getStringArray(R.array.pet_types)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, petTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.petTypeSpinner.adapter = adapter

        binding.saveButton.setOnClickListener {
            val petName = binding.petNameEditText.text.toString()
            val petType = binding.petTypeSpinner.selectedItem.toString()
            val petAge = binding.petAgeEditText.text.toString().toIntOrNull() ?: 0
            val petWeight = binding.petWeightEditText.text.toString().toFloatOrNull() ?: 0f

            val pet = Pet(
                name = petName,
                type = petType,
                age = petAge,
                weight = petWeight,
                lastVetVisit = System.currentTimeMillis().toString(),
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            petViewModel.insertPet(pet)

            // Показать toast сообщение
            Toast.makeText(requireContext(), "Питомец успешно добавлен", Toast.LENGTH_SHORT).show()

            // Перейти к ProfilesFragment
            findNavController().navigate(R.id.action_addPetFragment_to_profilesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
