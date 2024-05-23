package ru.mirea.guseva.fitpet.data.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.model.Pet
import ru.mirea.guseva.fitpet.databinding.ItemPetCardBinding

class PetAdapter(
    private var pets: List<Pet>,
    private val onItemClicked: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
    }

    override fun getItemCount() = pets.size

    inner class PetViewHolder(private val binding: ItemPetCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(pets[position])
                }
            }
        }

        fun bind(pet: Pet) {
            binding.pet = pet
            binding.executePendingBindings()
        }
    }

    fun updateData(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }
}
