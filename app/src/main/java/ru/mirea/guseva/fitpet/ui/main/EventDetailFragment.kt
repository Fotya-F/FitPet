package ru.mirea.guseva.fitpet.ui.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.databinding.FragmentEventDetailBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.EventViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EventDetailFragment : Fragment() {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private val args: EventDetailFragmentArgs by navArgs()
    private lateinit var currentEvent: Event

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventViewModel.getEventById(args.eventId).observe(viewLifecycleOwner) { event ->
            event?.let {
                currentEvent = it
                binding.eventName.setText(it.name)
                binding.eventDescription.setText(it.description)
                binding.eventDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it.eventTime ?: 0L)))
                binding.eventTime.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it.eventTime ?: 0L)))
                binding.eventImageUrl.setText(it.imageUrl)
                setupPetSpinner(it.animalName)
            }
        }

        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val hour = currentDate.get(Calendar.HOUR_OF_DAY)
        val minute = currentDate.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateFormatted = dateFormat.format(calendar.time)
                binding.eventDate.setText(dateFormatted)
            }, year, month, day
        )

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, selectedHour, selectedMinute ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                val timeFormatted = timeFormat.format(calendar.time)
                binding.eventTime.setText(timeFormatted)
            }, hour, minute, true
        )

        binding.eventDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                datePickerDialog.show()
                true
            } else {
                false
            }
        }

        binding.eventTime.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                timePickerDialog.show()
                true
            } else {
                false
            }
        }

        binding.saveButton.setOnClickListener {
            if (::currentEvent.isInitialized) {
                val updatedEvent = currentEvent.copy(
                    name = binding.eventName.text.toString(),
                    animalName = binding.petSpinner.selectedItem.toString(),
                    description = binding.eventDescription.text.toString(),
                    eventTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse("${binding.eventDate.text} ${binding.eventTime.text}")?.time,
                    imageUrl = binding.eventImageUrl.text.toString()
                )
                eventViewModel.updateEvent(updatedEvent)
                Toast.makeText(requireContext(), "Событие успешно обновлено", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.deleteButton.setOnClickListener {
            if (::currentEvent.isInitialized) {
                eventViewModel.deleteEvent(currentEvent)
                Toast.makeText(requireContext(), "Событие успешно удалено", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupPetSpinner(selectedPetName: String?) {
        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val petNames = pets.map { it.name }
            val petAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, petNames)
            petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.petSpinner.adapter = petAdapter
            selectedPetName?.let {
                val position = petAdapter.getPosition(it)
                binding.petSpinner.setSelection(position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
