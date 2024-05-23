package ru.mirea.guseva.fitpet.data.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.databinding.ItemStatusBinding

class StatusAdapter(
    private var statuses: List<String>
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

    fun updateData(newStatuses: List<String>) {
        statuses = newStatuses
        notifyDataSetChanged()
    }

    inner class StatusViewHolder(private val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(status: String) {
            binding.status = status
            binding.executePendingBindings()
        }
    }
}
