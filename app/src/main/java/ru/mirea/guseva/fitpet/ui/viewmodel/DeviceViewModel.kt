package ru.mirea.guseva.fitpet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mirea.guseva.fitpet.data.DeviceRepository
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    val devices: LiveData<List<SmartDevice>> = deviceRepository.allDevices.asLiveData()

    fun insertDevice(device: SmartDevice) {
        viewModelScope.launch {
            deviceRepository.insertDevice(device)
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

    fun getDeviceById(deviceId: Int) = deviceRepository.getDeviceById(deviceId).asLiveData()

    fun syncWithFirestore() {
        viewModelScope.launch {
            deviceRepository.syncWithFirestore()
        }
    }
}
