package ru.mirea.guseva.fitpet.ui.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.databinding.FragmentCareBinding
import ru.mirea.guseva.fitpet.ui.viewmodel.CareViewModel

@AndroidEntryPoint
class CareFragment : Fragment() {

    private var _binding: FragmentCareBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap

    private val careViewModel: CareViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPieChart()
    }

    private fun initPieChart() {
        val pieChart = binding.pieChart

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(55f))
        entries.add(PieEntry(25f))
        entries.add(PieEntry(15f))
        entries.add(PieEntry(5f))

        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(R.color.black)
        colors.add(R.color.teal_200)
        colors.add(R.color.purple_200)
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        pieChart.invalidate()
    }
}
