package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.DeviceRepository
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val devices = deviceRepository.getAllDevicesByUser(userId).asLiveData()

    fun insertDevice(device: SmartDevice) {
        viewModelScope.launch {
            deviceRepository.insertDevice(device.copy(userId = userId))
        }
    }

    fun updateDevice(device: SmartDevice) {
        viewModelScope.launch {
            deviceRepository.updateDevice(device)
        }
    }

    fun deleteDevice(device: SmartDevice) {
        viewModelScope.launch {
            deviceRepository.deleteDevice(device)
        }
    }

    fun getDeviceById(deviceId: Int) = deviceRepository.getDeviceByIdAndUser(deviceId, userId).asLiveData()
}
