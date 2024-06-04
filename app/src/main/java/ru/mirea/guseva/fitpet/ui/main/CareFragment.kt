package ru.mirea.guseva.fitpet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.databinding.FragmentCareBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.CareViewModel

@AndroidEntryPoint
class CareFragment : Fragment() {

    private var _binding: FragmentCareBinding? = null
    private val binding get() = _binding!!

    private val careViewModel: CareViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
