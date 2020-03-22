package com.relawan.kovidtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ContactDao{

    @Query("SELECT * from contacts where date(datetime(date / 1000 , 'unixepoch'))=date('now') ")
    fun todayList(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(contact: Contact): Long
}