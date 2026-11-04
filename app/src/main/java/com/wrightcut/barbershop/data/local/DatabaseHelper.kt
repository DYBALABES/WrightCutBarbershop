package com.wrightcut.barbershop.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class AdminAppointment(
    val id: String,
    val email: String,
    val phone: String,
    val service: String,
    var dateTime: String,
    var status: String
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "wright_cut_local.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
                email TEXT PRIMARY KEY,
                fullName TEXT,
                phone TEXT,
                passwordHash TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS bookings (
                localId INTEGER PRIMARY KEY AUTOINCREMENT,
                userId TEXT,
                email TEXT,
                phone TEXT,
                barberName TEXT,
                serviceName TEXT,
                price REAL,
                appointmentDate TEXT,
                appointmentTime TEXT,
                status TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS bookings")
        onCreate(db)
    }

    // USERS
    fun insertUser(user: LocalUser): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", user.email.lowercase().trim())
            put("fullName", user.fullName)
            put("phone", user.phone)
            put("passwordHash", user.passwordHash)
        }
        val result = db.insertWithOnConflict("users", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        return result != -1L
    }

    fun getUserByEmail(email: String): LocalUser? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1", arrayOf(email.trim()))
        var user: LocalUser? = null
        if (cursor.moveToFirst()) {
            user = LocalUser(
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                passwordHash = cursor.getString(cursor.getColumnIndexOrThrow("passwordHash"))
            )
        }
        cursor.close()
        return user
    }

    //BOOKINGS
    fun insertBooking(email: String, phone: String, service: String, date: String, time: String, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userId", email)
            put("email", email)
            put("phone", phone)
            put("barberName", "Kagiso & Boikhutso")
            put("serviceName", service)
            put("price", 100.0)
            put("appointmentDate", date)
            put("appointmentTime", time)
            put("status", status)
        }
        return db.insert("bookings", null, values)
    }

    fun getUserBookings(email: String): List<LocalBooking> {
        val list = mutableListOf<LocalBooking>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM bookings ORDER BY localId DESC", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    LocalBooking(
                        localId = cursor.getLong(cursor.getColumnIndexOrThrow("localId")),
                        userId = cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                        barberName = cursor.getString(cursor.getColumnIndexOrThrow("barberName")),
                        serviceName = cursor.getString(cursor.getColumnIndexOrThrow("serviceName")),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow("appointmentDate")),
                        appointmentTime = cursor.getString(cursor.getColumnIndexOrThrow("appointmentTime")),
                        status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllAdminAppointments(): List<AdminAppointment> {
        val list = mutableListOf<AdminAppointment>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM bookings ORDER BY localId DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("localId")).toString()
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val service = cursor.getString(cursor.getColumnIndexOrThrow("serviceName"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("appointmentDate"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("appointmentTime"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                list.add(
                    AdminAppointment(
                        id = id,
                        email = email,
                        phone = phone,
                        service = service,
                        dateTime = "$date @ $time",
                        status = status
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateBookingStatus(id: String, newStatus: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("status", newStatus)
        }
        db.update("bookings", values, "localId = ?", arrayOf(id))
    }
}