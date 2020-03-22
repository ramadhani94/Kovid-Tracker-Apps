package com.relawan.kovidtracker.data

class ContactRepository private constructor(private val contactDao: ContactDao){
    fun todayList() = contactDao.todayList()

    suspend fun save(ct: Contact) = contactDao.save(ct)

    companion object{
        @Volatile private var instance: ContactRepository? = null
        fun getInstance(contactDao: ContactDao) =
            instance ?: synchronized(this){
                instance ?: ContactRepository(contactDao).also { instance = it }
            }
    }
}