package ru.mirea.guseva.fitpet.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import ru.mirea.guseva.fitpet.data.local.DeviceDao
import ru.mirea.guseva.fitpet.data.local.entities.SmartDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(private val deviceDao: DeviceDao) {

    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("devices")

    val allDevices: Flow<List<SmartDevice>> = deviceDao.getAllDevices()

    suspend fun insertDevice(device: SmartDevice) {
        deviceDao.insertDevice(device)
        val firestoreDevice = device.copy(userId = FirebaseAuth.getInstance().currentUser?.uid)
        collection.add(firestoreDevice).await()
    }

    suspend fun updateDevice(device: SmartDevice) {
        deviceDao.updateDevice(device)
        collection.document(device.id.toString()).set(device).await()
    }

    suspend fun deleteDevice(device: SmartDevice) {
        deviceDao.deleteDevice(device)
        collection.document(device.id.toString()).delete().await()
    }

    fun getDeviceById(deviceId: Int): Flow<SmartDevice?> {
        return deviceDao.getDeviceById(deviceId)
    }

    suspend fun syncWithFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreDevices = collection.whereEqualTo("userId", userId).get().await().toObjects(SmartDevice::class.java)
        firestoreDevices.forEach { deviceDao.insertDevice(it) }
    }
}
