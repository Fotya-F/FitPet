package ru.mirea.guseva.fitpet.data.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.data.model.Device

class AddDeviceDialogFragment : DialogFragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_device, null)

            builder.setView(view)
                .setTitle(R.string.add_device)
                .setPositiveButton(R.string.add) { dialog, id ->
                    // Read input fields and add device
                    val name = view.findViewById<EditText>(R.id.device_name).text.toString()
                    val type = view.findViewById<EditText>(R.id.device_type).text.toString()
                    val petId = view.findViewById<EditText>(R.id.device_pet_id).text.toString().toInt()
                    val connectionStatus = view.findViewById<Switch>(R.id.device_connection_status).isChecked

                    val device = Device(name = name, type = type, petId = petId, connectionStatus = connectionStatus)
                    viewModel.insertDevice(device)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
