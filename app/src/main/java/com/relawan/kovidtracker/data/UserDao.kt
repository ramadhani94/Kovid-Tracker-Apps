package com.relawan.kovidtracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{

    @Query("SELECT EXISTS(SELECT 1 from user where id=1)")
    fun exist(): LiveData<Boolean>

    @Query("SELECT * from user where id=1")
    fun getUser(): LiveData<User>

    @Insert
    suspend fun save(user: User): Long
}