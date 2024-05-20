package ru.mirea.guseva.fitpet.data.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.guseva.fitpet.databinding.FragmentCalendarBinding
import ru.mirea.guseva.fitpet.data.ui.adapters.EventAdapter

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val TAG = "CalendarFragment"

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

        val recyclerView = binding.recyclerViewEvents
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData
        calendarViewModel.allEvents.observe(viewLifecycleOwner, { events ->
            Log.d(TAG, "Events observed: ${events.size}")
            recyclerView.adapter = EventAdapter(events) { event ->
                // Handle item click here
            }
            Log.d(TAG, "Adapter set with ${events.size} items")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
