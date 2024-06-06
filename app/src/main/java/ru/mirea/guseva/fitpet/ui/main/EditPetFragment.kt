package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentEditPetBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel
import ru.mirea.guseva.fitpet.data.local.entities.Pet

@AndroidEntryPoint
class EditPetFragment : Fragment() {

    private var _binding: FragmentEditPetBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val args: EditPetFragmentArgs by navArgs()

    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == value) {
                return i
            }
        }
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the spinner with pet types
        val petTypes = resources.getStringArray(R.array.pet_types)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, petTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.petTypeSpinner.adapter = adapter

        val petId = args.petId
        petViewModel.getPetById(petId).observe(viewLifecycleOwner) { pet ->
            pet?.let { currentPet ->
                // Update the UI with the current pet data
                binding.petNameEditText.setText(currentPet.name)
                binding.petTypeSpinner.setSelection(getSpinnerIndex(binding.petTypeSpinner, currentPet.type))
                binding.petAgeEditText.setText(currentPet.age.toString())
                binding.petWeightEditText.setText(currentPet.weight.toString())

                binding.saveButton.setOnClickListener {
                    val updatedPet = Pet(
                        id = currentPet.id,
                        name = binding.petNameEditText.text.toString(),
                        type = binding.petTypeSpinner.selectedItem.toString(),
                        age = binding.petAgeEditText.text.toString().toIntOrNull() ?: currentPet.age,
                        weight = binding.petWeightEditText.text.toString().toFloatOrNull() ?: currentPet.weight,
                        lastVetVisit = currentPet.lastVetVisit,
                        userId = currentPet.userId
                    )
                    petViewModel.updatePet(updatedPet)
                    Toast.makeText(requireContext(), "Питомец успешно обновлен", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditPetFragmentDirections.actionEditPetFragmentToProfilesFragment())
                }

                binding.deleteButton.setOnClickListener {
                    petViewModel.deletePet(currentPet)
                    Toast.makeText(requireContext(), "Питомец успешно удален", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditPetFragmentDirections.actionEditPetFragmentToProfilesFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
