package ru.mirea.guseva.fitpet.data.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.DeviceAdapter
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setupRecyclerViews()
        observeData()
        setupNavigationObservers()
    }

    private fun setupNavigationObservers() {
        profileViewModel.navigateToAddPet.observe(viewLifecycleOwner) {
            if (it) {
                showAddPetDialog()
                profileViewModel.onAddPetNavigated()
            }
        }

        profileViewModel.navigateToAddDevice.observe(viewLifecycleOwner) {
            if (it) {
                showAddDeviceDialog()
                profileViewModel.onAddDeviceNavigated()
            }
        }
    }

    private fun showAddPetDialog() {
        val dialog = AddPetDialogFragment()
        dialog.show(parentFragmentManager, "AddPetDialog")
    }

    private fun showAddDeviceDialog() {
        val dialog = AddDeviceDialogFragment()
        dialog.show(parentFragmentManager, "AddDeviceDialog")
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewPets.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPets.adapter = PetAdapter(emptyList()) { pet ->
            // Handle pet click here
        }

        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDevices.adapter = DeviceAdapter(emptyList()) { device ->
            // Handle device click here
        }
    }

    private fun observeData() {
        profileViewModel.allPets.observe(viewLifecycleOwner) { pets ->
            (binding.recyclerViewPets.adapter as PetAdapter).updateData(pets)
        }

        profileViewModel.allDevices.observe(viewLifecycleOwner) { devices ->
            (binding.recyclerViewDevices.adapter as DeviceAdapter).updateData(devices)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
