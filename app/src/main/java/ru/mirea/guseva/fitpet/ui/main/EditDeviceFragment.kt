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
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentEditDeviceBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.DeviceViewModel
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel

@AndroidEntryPoint
class EditDeviceFragment : Fragment() {

    private var _binding: FragmentEditDeviceBinding? = null
    private val binding get() = _binding!!

    private val deviceViewModel: DeviceViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private val args: EditDeviceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDeviceBinding.inflate(inflater, container, false)
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

        val deviceId = args.deviceId
        deviceViewModel.getDeviceById(deviceId).observe(viewLifecycleOwner) { device ->
            device?.let { currentDevice ->
                binding.deviceNameEditText.setText(currentDevice.name)
                binding.deviceTypeSpinner.setSelection(deviceTypes.indexOf(currentDevice.type))
                binding.petSpinner.setSelection(petViewModel.pets.value?.indexOfFirst { it.id == currentDevice.petId } ?: 0)

                binding.saveButton.setOnClickListener {
                    val selectedPetName = binding.petSpinner.selectedItem.toString()
                    val selectedPet = petViewModel.pets.value?.firstOrNull { it.name == selectedPetName }
                    if (selectedPet != null) {
                        val updatedDevice = SmartDevice(
                            id = currentDevice.id,
                            name = binding.deviceNameEditText.text.toString(),
                            type = binding.deviceTypeSpinner.selectedItem.toString(),
                            petId = selectedPet.id,
                            isConnected = currentDevice.isConnected
                        )
                        deviceViewModel.updateDevice(updatedDevice)
                        Toast.makeText(requireContext(), "Устройство успешно обновлено", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(EditDeviceFragmentDirections.actionEditDeviceFragmentToProfilesFragment())
                    }
                }

                binding.deleteButton.setOnClickListener {
                    deviceViewModel.deleteDevice(currentDevice)
                    Toast.makeText(requireContext(), "Устройство успешно удалено", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditDeviceFragmentDirections.actionEditDeviceFragmentToProfilesFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
