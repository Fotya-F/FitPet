package ru.mirea.guseva.fitpet.data.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.model.Device
import ru.mirea.guseva.fitpet.databinding.ItemDeviceBinding

class DeviceAdapter(
    private var devices: List<Device>,
    private val onItemClicked: (Device) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device)
    }

    override fun getItemCount() = devices.size

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(devices[position])
                }
            }
        }

        fun bind(device: Device) {
            binding.device = device
            binding.executePendingBindings()
        }
    }

    fun updateData(newDevices: List<Device>) {
        devices = newDevices
        notifyDataSetChanged()
    }
}
