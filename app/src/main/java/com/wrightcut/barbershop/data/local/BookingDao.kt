package com.wrightcut.barbershop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: LocalBooking): Long

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY localId DESC")
    suspend fun getUserBookings(userId: String): List<LocalBooking>

    @Query("SELECT * FROM bookings WHERE isSynced = 0")
    suspend fun getUnsyncedBookings(): List<LocalBooking>

    @Update
    suspend fun updateBooking(booking: LocalBooking)
}