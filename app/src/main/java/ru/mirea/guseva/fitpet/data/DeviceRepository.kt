package ru.mirea.guseva.fitpet.data

import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.DeviceDao
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(private val deviceDao: DeviceDao) {

    fun getAllDevicesByUser(userId: String): Flow<List<SmartDevice>> {
        return deviceDao.getAllDevicesByUser(userId)
    }

    suspend fun insertDevice(device: SmartDevice) {
        deviceDao.insertDevice(device)
    }

    suspend fun updateDevice(device: SmartDevice) {
        deviceDao.updateDevice(device)
    }

    suspend fun deleteDevice(device: SmartDevice) {
        deviceDao.deleteDevice(device)
    }

    fun getDeviceByIdAndUser(deviceId: Int, userId: String): Flow<SmartDevice?> {
        return deviceDao.getDeviceByIdAndUser(deviceId, userId)
    }
}
