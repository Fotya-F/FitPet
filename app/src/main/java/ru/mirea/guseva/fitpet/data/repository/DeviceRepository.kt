package ru.mirea.guseva.fitpet.data.repository

import androidx.lifecycle.LiveData
import ru.mirea.guseva.fitpet.data.local.DeviceDao
import ru.mirea.guseva.fitpet.data.model.Device

class DeviceRepository(private val deviceDao: DeviceDao) {

    val allDevices: LiveData<List<Device>> = deviceDao.getAllDevices()

    suspend fun insert(device: Device) {
        deviceDao.insert(device)
    }

    suspend fun update(device: Device) {
        deviceDao.update(device)
    }

    suspend fun delete(device: Device) {
        deviceDao.delete(device)
    }
}
