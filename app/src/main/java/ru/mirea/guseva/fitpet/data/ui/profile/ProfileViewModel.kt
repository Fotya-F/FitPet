package ru.mirea.guseva.fitpet.data.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.mirea.guseva.fitpet.data.local.AppDatabase
import ru.mirea.guseva.fitpet.data.model.Device
import ru.mirea.guseva.fitpet.data.model.Pet
import ru.mirea.guseva.fitpet.data.repository.DeviceRepository
import ru.mirea.guseva.fitpet.data.repository.PetRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val petDao = AppDatabase.getDatabase(application).petDao()
    private val deviceDao = AppDatabase.getDatabase(application).deviceDao()
    private val petRepository: PetRepository = PetRepository(petDao)
    private val deviceRepository: DeviceRepository = DeviceRepository(deviceDao)

    val allPets: LiveData<List<Pet>> = petRepository.allPets
    val allDevices: LiveData<List<Device>> = deviceRepository.allDevices

    fun insertPet(pet: Pet) = viewModelScope.launch {
        petRepository.insert(pet)
    }

    fun updatePet(pet: Pet) = viewModelScope.launch {
        petRepository.update(pet)
    }

    fun deletePet(pet: Pet) = viewModelScope.launch {
        petRepository.delete(pet)
    }

    fun insertDevice(device: Device) = viewModelScope.launch {
        deviceRepository.insert(device)
    }

    fun updateDevice(device: Device) = viewModelScope.launch {
        deviceRepository.update(device)
    }

    fun deleteDevice(device: Device) = viewModelScope.launch {
        deviceRepository.delete(device)
    }
}
