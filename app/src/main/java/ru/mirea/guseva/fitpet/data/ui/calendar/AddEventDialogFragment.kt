package ru.mirea.guseva.fitpet.data.ui.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.mirea.guseva.fitpet.data.model.Event
import ru.mirea.guseva.fitpet.databinding.DialogAddEventBinding
import java.util.Calendar

class AddEventDialogFragment : DialogFragment() {

    private val viewModel: AddEventViewModel by viewModels()
    private var _binding: DialogAddEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editTextDate.setOnClickListener { showDatePickerDialog() }
        binding.editTextTime.setOnClickListener { showTimePickerDialog() }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val date = binding.editTextDate.text.toString()
            val time = binding.editTextTime.text.toString()

            if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val event = Event(
                    title = title,
                    description = description,
                    date = date,
                    time = time,
                    petId = 1 // Замените на актуальный ID питомца
                )
                viewModel.insert(event)
                dismiss()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.editTextDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            binding.editTextTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
