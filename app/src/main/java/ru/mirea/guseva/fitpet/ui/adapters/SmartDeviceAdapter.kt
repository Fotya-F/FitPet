package ru.mirea.guseva.fitpet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.databinding.ItemSmartDeviceBinding
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice

class SmartDeviceAdapter(private val clickListener: (SmartDevice) -> Unit) :
    RecyclerView.Adapter<SmartDeviceAdapter.SmartDeviceViewHolder>() {

    private var devices: List<SmartDevice> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartDeviceViewHolder {
        val binding = ItemSmartDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmartDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmartDeviceViewHolder, position: Int) {
        holder.bind(devices[position], clickListener)
    }

    override fun getItemCount(): Int = devices.size

    fun submitList(newDevices: List<SmartDevice>) {
        devices = newDevices
        notifyDataSetChanged()
    }

    class SmartDeviceViewHolder(private val binding: ItemSmartDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: SmartDevice, clickListener: (SmartDevice) -> Unit) {
            binding.device = device
            binding.executePendingBindings()
            binding.root.setOnClickListener { clickListener(device) }
        }
    }
}
