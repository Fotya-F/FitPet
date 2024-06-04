package ru.mirea.guseva.fitpet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice

@Dao
interface DeviceDao {
    @Query("SELECT * FROM smart_devices")
    fun getAllDevices(): Flow<List<SmartDevice>>

    @Query("SELECT * FROM smart_devices WHERE id = :deviceId")
    fun getDeviceById(deviceId: Int): Flow<SmartDevice?>

    @Insert
    suspend fun insertDevice(device: SmartDevice)

    @Update
    suspend fun updateDevice(device: SmartDevice)

    @Delete
    suspend fun deleteDevice(device: SmartDevice)
}
