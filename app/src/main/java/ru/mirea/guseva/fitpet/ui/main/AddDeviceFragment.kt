package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentAddDeviceBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.DeviceViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice

@AndroidEntryPoint
class AddDeviceFragment : Fragment() {

    private var _binding: FragmentAddDeviceBinding? = null
    private val binding get() = _binding!!

    private val deviceViewModel: DeviceViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deviceTypes = resources.getStringArray(R.array.device_types)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, deviceTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.deviceTypeSpinner.adapter = adapter

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val petNames = pets.map { it.name }
            val petAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, petNames)
            petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.petSpinner.adapter = petAdapter
        }

        binding.saveButton.setOnClickListener {
            val deviceName = binding.deviceNameEditText.text.toString()
            val deviceType = binding.deviceTypeSpinner.selectedItem.toString()
            val petName = binding.petSpinner.selectedItem.toString()

            lifecycleScope.launch {
                val pet = petViewModel.getPetByName(petName)
                if (pet != null) {
                    val device = SmartDevice(
                        name = deviceName,
                        type = deviceType,
                        petId = pet.id,
                        isConnected = true,
                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    )
                    deviceViewModel.insertDevice(device)

                    deviceViewModel.insertDevice(device)
                    Toast.makeText(requireContext(), "Устройство успешно добавлено", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(AddDeviceFragmentDirections.actionAddDeviceFragmentToProfilesFragment())
                } else {
                    Toast.makeText(requireContext(), "Питомец с таким именем не найден", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

