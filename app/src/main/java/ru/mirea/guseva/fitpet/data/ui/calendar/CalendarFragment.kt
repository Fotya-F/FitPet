package ru.mirea.guseva.fitpet.data.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.data.ui.adapters.EventAdapter
import ru.mirea.guseva.fitpet.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        setupRecyclerView()
        setupFab()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEvents.adapter = EventAdapter(emptyList()) { event ->
            // Handle item click here
        }
    }

    private fun setupFab() {
        binding.fabAddEvent.setOnClickListener {
            AddEventDialogFragment().show(childFragmentManager, "AddEventDialogFragment")
        }
    }

    private fun observeData() {
        calendarViewModel.allEvents.observe(viewLifecycleOwner) { events ->
            (binding.recyclerViewEvents.adapter as EventAdapter).updateData(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
