package ru.mirea.guseva.fitpet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.DeviceDao
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(private val deviceDao: DeviceDao) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("devices")

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

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreDevices = deviceDao.getAllDevicesByUser(userId).firstOrNull()?.let { devices ->
            devices.map { it.copy(userId = userId) }
        } ?: emptyList()
        firestoreDevices.forEach { device ->
            val deviceRef = collection.document(device.id.toString())
            deviceRef.set(device).await()
        }
    }

    suspend fun restoreFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreDevices = collection.whereEqualTo("userId", userId).get().await().toObjects(SmartDevice::class.java)
        firestoreDevices.forEach { device ->
            deviceDao.insertDevice(device)
        }
    }
}
