package com.relawan.kovidtracker.data

class UserRepository private constructor(private val userDao: UserDao){

    fun getUser() = userDao.getUser()
    suspend fun save(ct: User) = userDao.save(ct)
    companion object{
        @Volatile private var instance: UserRepository? = null
        fun getInstance(userDao: UserDao) =
            instance ?: synchronized(this){
                instance ?: UserRepository(userDao).also { instance = it }
            }
    }
}