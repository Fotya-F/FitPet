package ru.mirea.guseva.fitpet.data.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.guseva.fitpet.data.model.Plan
import ru.mirea.guseva.fitpet.databinding.ItemPlanBinding

class PlanAdapter(
    private var plans: List<Plan>
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.bind(plan)
    }

    override fun getItemCount() = plans.size

    inner class PlanViewHolder(private val binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: Plan) {
            binding.plan = plan
            binding.executePendingBindings()
        }
    }

    fun updateData(newPlans: List<Plan>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}
