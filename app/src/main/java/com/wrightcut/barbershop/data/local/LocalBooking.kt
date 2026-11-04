package com.wrightcut.barbershop.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class LocalBooking(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val userId: String,
    val barberName: String,
    val serviceName: String,
    val price: Double,
    val appointmentDate: String,
    val appointmentTime: String,
    val status: String = "Confirmed",
    val isSynced: Boolean = true
)