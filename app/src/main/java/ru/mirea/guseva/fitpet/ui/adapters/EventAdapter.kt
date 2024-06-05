package ru.mirea.guseva.fitpet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mirea.guseva.fitpet.databinding.ItemEventBinding
import ru.mirea.guseva.fitpet.data.local.entities.Event
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private var events: List<Event>, private val clickListener: (Event) -> Unit) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position], clickListener)
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, clickListener: (Event) -> Unit) {
            binding.eventDescription.text = event.description
            binding.eventTime.text = event.eventTime?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it)) }
            binding.eventDate.text = getFormattedDate(event.eventTime)
            Glide.with(binding.root.context).load(event.imageUrl).into(binding.imageView)

            binding.root.setOnClickListener { clickListener(event) }
        }

        private fun getFormattedDate(time: Long?): String {
            time ?: return ""
            val eventDate = Calendar.getInstance().apply { timeInMillis = time }
            val today = Calendar.getInstance()

            return when {
                today.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) == eventDate.get(Calendar.DAY_OF_YEAR) -> {
                    "Сегодня"
                }

                today.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) + 1 == eventDate.get(Calendar.DAY_OF_YEAR) -> {
                    "Завтра"
                }

                else -> {
                    SimpleDateFormat("d MMM", Locale.getDefault()).format(Date(time))
                }
            }
        }
    }
}
