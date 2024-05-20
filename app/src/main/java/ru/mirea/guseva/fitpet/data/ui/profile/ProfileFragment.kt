package ru.mirea.guseva.fitpet.data.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.databinding.FragmentProfileBinding
import ru.mirea.guseva.fitpet.data.ui.adapters.DeviceAdapter
import ru.mirea.guseva.fitpet.data.ui.adapters.PetAdapter

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ProfileFragment"

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

        val recyclerViewPets = binding.recyclerViewPets
        recyclerViewPets.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        profileViewModel.allPets.observe(viewLifecycleOwner, { pets ->
            Log.d(TAG, "Pets observed: ${pets.size}")
            recyclerViewPets.adapter = PetAdapter(pets) { pet ->
                // Handle item click here
            }
            Log.d(TAG, "Adapter set with ${pets.size} items")
        })

        val recyclerViewDevices = binding.recyclerViewDevices
        recyclerViewDevices.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        profileViewModel.allDevices.observe(viewLifecycleOwner, { devices ->
            Log.d(TAG, "Devices observed: ${devices.size}")
            recyclerViewDevices.adapter = DeviceAdapter(devices) { device ->
                // Handle item click here
            }
            Log.d(TAG, "Adapter set with ${devices.size} items")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
