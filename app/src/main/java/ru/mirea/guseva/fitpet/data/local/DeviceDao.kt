package ru.mirea.guseva.fitpet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.mirea.guseva.fitpet.data.model.Device

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices")
    fun getAllDevices(): LiveData<List<Device>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: Device)

    @Update
    suspend fun update(device: Device)

    @Delete
    suspend fun delete(device: Device)
}
