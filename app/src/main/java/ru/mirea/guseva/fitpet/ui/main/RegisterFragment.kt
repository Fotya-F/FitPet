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
import ru.mirea.guseva.fitpet.databinding.FragmentRegisterBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.registerUser(email, password) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show()
                    if (isAdded && isResumed) {
                        findNavController().navigate(R.id.action_registerFragment_to_main_graph)
                    }
                } else {
                    Toast.makeText(requireContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
