package ru.mirea.guseva.fitpet.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.mirea.guseva.fitpet.R

class AddChoiceDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выберите действие")
            .setItems(arrayOf("Добавить питомца", "Добавить устройство")) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(R.id.action_addChoiceDialogFragment_to_addPetFragment)
                    1 -> findNavController().navigate(R.id.action_addChoiceDialogFragment_to_addDeviceFragment)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}
