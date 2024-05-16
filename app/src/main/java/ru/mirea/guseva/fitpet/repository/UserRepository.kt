package ru.mirea.guseva.fitpet.repository

import ru.mirea.guseva.fitpet.database.dao.UserDao
import ru.mirea.guseva.fitpet.model.UserProfile

class UserRepository(private val userDao: UserDao) {
    fun getUser(): UserProfile {
        val user = userDao.getAllUsers().first()
        return UserProfile(user.id, user.username, user.email)
    }
}
