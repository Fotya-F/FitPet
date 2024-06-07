package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentProfileSettingsBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.ArticleViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.AuthViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.DeviceViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.EventViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {
    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private val deviceViewModel: DeviceViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {
            authViewModel.logoutUser()
            Toast.makeText(requireContext(), "Вы успешно вышли из учетной записи", Toast.LENGTH_SHORT).show()
            if (isAdded && isResumed) {
                findNavController().navigate(R.id.action_profileSettingsFragment_to_auth_graph)
            }
        }

        binding.saveToFirestoreButton.setOnClickListener {
            petViewModel.syncWithFirestore()
            deviceViewModel.syncWithFirestore()
            eventViewModel.syncWithFirestore()
            articleViewModel.syncWithFirestore()
            Toast.makeText(requireContext(), "Данные сохранены в Firestore", Toast.LENGTH_SHORT).show()
        }

        binding.restoreFromFirestoreButton.setOnClickListener {
            petViewModel.restoreFromFirestore()
            deviceViewModel.restoreFromFirestore()
            eventViewModel.restoreFromFirestore()
            articleViewModel.restoreFromFirestore()
            Toast.makeText(requireContext(), "Данные восстановлены из Firestore", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}