package ru.mirea.guseva.fitpet.data.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.mirea.guseva.fitpet.R
import ru.mirea.guseva.fitpet.data.model.Pet

class AddPetDialogFragment : DialogFragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_pet, null)

            builder.setView(view)
                .setTitle(R.string.add_pet)
                .setPositiveButton(R.string.add) { dialog, id ->
                    // Read input fields and add pet
                    val name = view.findViewById<EditText>(R.id.pet_name).text.toString()
                    val gender = view.findViewById<EditText>(R.id.pet_gender).text.toString()
                    val age = view.findViewById<EditText>(R.id.pet_age).text.toString().toInt()
                    val weight = view.findViewById<EditText>(R.id.pet_weight).text.toString().toDouble()
                    val species = view.findViewById<EditText>(R.id.pet_species).text.toString()
                    val breed = view.findViewById<EditText>(R.id.pet_breed).text.toString()
                    val avatarUrl = view.findViewById<EditText>(R.id.pet_avatar_url).text.toString()

                    val pet = Pet(name = name, gender = gender, age = age, weight = weight, species = species, breed = breed, avatarUrl = avatarUrl, status = "Normal", state = "Green", plans = emptyList(), checks = emptyList())
                    viewModel.insertPet(pet)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
