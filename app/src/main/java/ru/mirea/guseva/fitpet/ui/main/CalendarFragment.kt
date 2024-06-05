package ru.mirea.guseva.fitpet.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.local.entities.Event
import ru.mirea.guseva.fitpet.databinding.DialogAddEventBinding
import ru.mirea.guseva.fitpet.databinding.FragmentCalendarBinding
import ru.mirea.guseva.fitpet.ui.adapters.EventAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.CalendarViewModel
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
            // Обработка кликов по элементам событий
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
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateFormatted = dateFormat.format(calendar.time)
                dialogBinding.eventDateInput.setText(dateFormatted)
            },
            year, month, day
        )

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val timeFormatted = timeFormat.format(calendar.time)
                dialogBinding.eventTimeInput.setText(timeFormatted)
            },
            hour, minute, true
        )

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Добавить") { _, _ ->
                val animalName = dialogBinding.animalNameInput.text.toString()
                val description = dialogBinding.descriptionInput.text.toString()
                val eventDate = dialogBinding.eventDateInput.text.toString()
                val eventTime = dialogBinding.eventTimeInput.text.toString()
                val imageUrl = dialogBinding.imageUrlInput.text.toString()

                val dateTime = "$eventDate $eventTime"

                val event = Event(
                    animalName = animalName,
                    description = description,
                    eventTime = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).parse(dateTime)?.time ?: 0L, // Используем 0L как значение по умолчанию
                    imageUrl = imageUrl
                )
                calendarViewModel.insert(event)
                scheduleNotification(requireContext(), event)
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()

        dialogBinding.eventDateInput.setOnClickListener {
            datePickerDialog.show()
        }

        dialogBinding.eventTimeInput.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun scheduleNotification(context: Context, event: Event) {
        val workManager = WorkManager.getInstance(context)
        val data = Data.Builder()
            .putInt("event_id", event.id)
            .putString("animal_name", event.animalName)
            .putLong("event_time", event.eventTime ?: 0L) // Проверка на null и использование 0L как значение по умолчанию
            .build()

        val delay = event.eventTime?.let { it - System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) } ?: 0L
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueue(notificationWork)
    }
}
