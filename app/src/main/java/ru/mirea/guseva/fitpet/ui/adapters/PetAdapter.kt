package ru.mirea.guseva.fitpet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.databinding.ItemPetBinding

class PetAdapter(private val clickListener: (Pet) -> Unit) : ListAdapter<Pet, PetAdapter.PetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = getItem(position)
        holder.bind(pet, clickListener)
    }

    class PetViewHolder(private val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet, clickListener: (Pet) -> Unit) {
            binding.pet = pet
            binding.executePendingBindings()
            binding.root.setOnClickListener { clickListener(pet) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem == newItem
        }
    }

    fun updateData(newPets: List<Pet>) {
        submitList(newPets)
    }
}
