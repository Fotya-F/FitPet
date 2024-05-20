package ru.mirea.guseva.fitpet.data.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.data.model.Event
import ru.mirea.guseva.fitpet.databinding.FragmentAddEventBinding

class AddEventFragment : Fragment() {

    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val date = binding.editTextDate.text.toString()
            val time = binding.editTextTime.text.toString()

            val event = Event(title = title, description = description, date = date, time = time, petId = 1) // Replace petId with actual ID
            viewModel.insert(event)

            findNavController().navigate(R.id.action_addEventFragment_to_calendarFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
