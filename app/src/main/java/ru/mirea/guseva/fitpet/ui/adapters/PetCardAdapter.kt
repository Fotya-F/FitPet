package ru.mirea.guseva.fitpet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.databinding.ItemPetCardBinding

class PetCardAdapter(
    private var pets: List<Pet>,
    private val clickListener: (Pet) -> Unit
) : RecyclerView.Adapter<PetCardAdapter.PetCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetCardViewHolder {
        val binding = ItemPetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetCardViewHolder, position: Int) {
        holder.bind(pets[position], clickListener)
    }

    override fun getItemCount(): Int = pets.size

    fun updateData(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

    class PetCardViewHolder(private val binding: ItemPetCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet, clickListener: (Pet) -> Unit) {
            binding.pet = pet
            binding.executePendingBindings()
            binding.root.setOnClickListener { clickListener(pet) }
        }
    }
}
