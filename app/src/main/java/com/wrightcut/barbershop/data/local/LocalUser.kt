package com.wrightcut.barbershop.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUser(
    @PrimaryKey val email: String,
    val fullName: String,
    val phone: String,
    val passwordHash: String
)