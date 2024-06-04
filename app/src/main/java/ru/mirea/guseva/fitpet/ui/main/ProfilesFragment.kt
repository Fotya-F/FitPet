package ru.mirea.guseva.fitpet.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.databinding.FragmentProfilesBinding
import ru.mirea.guseva.fitpet.ui.adapters.PetAdapter
import ru.mirea.guseva.fitpet.ui.adapters.SmartDeviceAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.DeviceViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel

@AndroidEntryPoint
class ProfilesFragment : Fragment() {

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by viewModels()
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var petAdapter: PetAdapter
    private lateinit var deviceAdapter: SmartDeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            showAddOptionsDialog()
        }

        petAdapter = PetAdapter { pet ->
            findNavController().navigate(ProfilesFragmentDirections.actionProfilesFragmentToEditPetFragment(pet.id))
        }

        deviceAdapter = SmartDeviceAdapter { device ->
            findNavController().navigate(ProfilesFragmentDirections.actionProfilesFragmentToEditDeviceFragment(device.id))
        }

        binding.recyclerViewPets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = petAdapter
        }

        binding.recyclerViewDevices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deviceAdapter
        }

        petViewModel.pets.observe(viewLifecycleOwner, Observer { pets ->
            petAdapter.submitList(pets)
        })

        deviceViewModel.devices.observe(viewLifecycleOwner, Observer { devices ->
            deviceAdapter.submitList(devices)
        })

        // Добавляем кнопку для перехода в настройки профиля
        binding.profileSettingsButton.setOnClickListener {
            findNavController().navigate(ProfilesFragmentDirections.actionProfilesFragmentToProfileSettingsFragment())
        }
    }

    private fun showAddOptionsDialog() {
        val options = arrayOf("Добавить питомца", "Добавить устройство")
        AlertDialog.Builder(requireContext())
            .setTitle("Выберите опцию")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(ProfilesFragmentDirections.actionProfilesFragmentToAddPetFragment())
                    1 -> findNavController().navigate(ProfilesFragmentDirections.actionProfilesFragmentToAddDeviceFragment())
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
