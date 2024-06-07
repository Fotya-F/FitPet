package ru.mirea.guseva.fitpet.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.data.local.entities.Pet
import ru.mirea.guseva.fitpet.databinding.FragmentCareBinding
import ru.mirea.guseva.fitpet.ui.adapters.PetCardAdapter
import ru.mirea.guseva.fitpet.ui.viewmodel.CareViewModel
import java.security.KeyStore

private var selectedPet: Pet? = null

@AndroidEntryPoint
class CareFragment : Fragment() {
    private var _binding: FragmentCareBinding? = null
    private val binding get() = _binding!!
    private lateinit var petAdapter: PetCardAdapter
    private val careViewModel: CareViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petAdapter = PetCardAdapter(emptyList()) { pet ->
            selectedPet = pet
            updatePieChartAndRecommendations(pet)
            careViewModel.updateStatisticsIfNeeded(pet.id)
        }
        binding.rvPets.adapter = petAdapter
        binding.rvPets.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        initPieChart()
        careViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petAdapter.updateData(pets)
            if (pets.isNotEmpty()) {
                updatePieChartAndRecommendations(pets.first())
            }
        }
    }

    private fun initPieChart() {
        val pieChart = binding.pieChart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61f
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val label = (e as? PieEntry)?.label
                binding.tvRecommendations.text = careViewModel.getRecommendationsForLabel(label)
            }

            override fun onNothingSelected() {
                // Do nothing
            }
        })
    }

    private fun updatePieChartAndRecommendations(pet: Pet) {
        selectedPet?.let {
            val pieChart = binding.pieChart
            val sleepValue = careViewModel.getSleepValue(it.id)
            val activityValue = careViewModel.getActivityValue(it.id)
            val weightValue = careViewModel.getWeightValue(it.id)
            val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(sleepValue, "Сон"))
        entries.add(PieEntry(activityValue, "Активность"))
        entries.add(PieEntry(weightValue, "Вес"))
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.GRAY, Color.rgb(0, 192, 192), Color.rgb(200, 112, 200))
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        pieChart.data = data
        pieChart.invalidate()
            binding.tvRecommendations.text = careViewModel.getRecommendations(it.id)
        } ?: run {
            // Очистить график и рекомендации, если питомец не выбран
            binding.pieChart.clear()
            binding.tvRecommendations.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}