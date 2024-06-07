package ru.mirea.guseva.fitpet.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.databinding.DialogAddEventBinding
import ru.mirea.guseva.fitpet.databinding.FragmentCalendarBinding
import ru.mirea.guseva.fitpet.ui.adapters.EventAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.CalendarViewModel
import ru.mirea.guseva.fitpet.ui.viewmodel.PetViewModel
import ru.mirea.guseva.fitpet.utils.NotificationWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val calendarViewModel: CalendarViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventAdapter = EventAdapter(emptyList()) { event ->
            // Navigate to EventDetailFragment
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToEventDetailFragment(event.id))
        }
        binding.recyclerView.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(context)
        }
        lifecycleScope.launch {
            calendarViewModel.allEvents.collect { events ->
                eventAdapter.updateData(events)
            }
        }
        binding.addButton.setOnClickListener {
            showAddEventDialog()
        }
        lifecycleScope.launch {
            calendarViewModel.deleteOldEvents()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddEventDialog() {
        val dialogBinding = DialogAddEventBinding.inflate(layoutInflater)
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
                dialogBinding.eventDateInput.setText(dateFormatted)
            }, year, month, day
        )

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, selectedHour, selectedMinute ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                val timeFormatted = timeFormat.format(calendar.time)
                dialogBinding.eventTimeInput.setText(timeFormatted)
            }, hour, minute, true
        )

        // Load pets from ViewModel and set up the spinner
        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            val petNames = pets.map { it.name }
            val petAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, petNames)
            petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.petSpinner.adapter = petAdapter
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Добавить") { _, _ ->
                val selectedPetName = dialogBinding.petSpinner.selectedItem.toString()
                val description = dialogBinding.eventDescription.text.toString()
                val eventDate = dialogBinding.eventDateInput.text.toString()
                val eventTime = dialogBinding.eventTimeInput.text.toString()
                val imageUrl = dialogBinding.imageUrlInput.text.toString()
                val dateTime = "$eventDate $eventTime"
                lifecycleScope.launch {
                    val pet = petViewModel.getPetByName(selectedPetName)
                    if (pet != null) {
                        val event = Event(
                            petId = pet.id,
                            animalName = pet.name,
                            description = description,
                            eventTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(dateTime)?.time ?: 0L,
                            imageUrl = imageUrl,
                            userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        )
                        calendarViewModel.insert(event)
                        scheduleNotification(requireContext(), event)
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()

        dialogBinding.eventDateInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                datePickerDialog.show()
                true
            } else {
                false
            }
        }

        dialogBinding.eventTimeInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                timePickerDialog.show()
                true
            } else {
                false
            }
        }
    }

    private fun scheduleNotification(context: Context, event: Event) {
        val workManager = WorkManager.getInstance(context)
        val data = Data.Builder()
            .putInt("event_id", event.id)
            .putString("animal_name", event.animalName)
            .putLong("event_time", event.eventTime ?: 0L)
            .build()
        val delay = event.eventTime?.let { it - System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) } ?: 0L
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        workManager.enqueue(notificationWork)
    }
}
