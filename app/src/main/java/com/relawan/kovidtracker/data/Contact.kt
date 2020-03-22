package com.relawan.kovidtracker.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contacts", indices = [Index(value = ["bluetooth_id", "date"],
    unique = true)]
)
data class Contact(
    @ColumnInfo(name = "bluetooth_id")
    val bluetoothId: String,
    val longitude: Double?,
    val latitude: Double?,
    val date: Calendar = Calendar.getInstance()
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = 0
}