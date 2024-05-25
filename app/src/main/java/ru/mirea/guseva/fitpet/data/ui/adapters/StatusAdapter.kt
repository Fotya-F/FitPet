package ru.mirea.guseva.fitpet.data.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.model.Status
import ru.mirea.guseva.fitpet.databinding.ItemStatusBinding

class StatusAdapter(
    private var statuses: List<Status>
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statuses[position]
        holder.bind(status)
    }

    override fun getItemCount() = statuses.size

    inner class StatusViewHolder(private val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(status: Status) {
            binding.status = status
            binding.executePendingBindings()
        }
    }

    fun updateData(newStatuses: List<Status>) {
        statuses = newStatuses
        notifyDataSetChanged()
    }
}
