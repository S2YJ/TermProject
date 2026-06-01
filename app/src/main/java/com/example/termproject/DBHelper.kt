package com.example.termproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Travel(val no: Int, val place: String, val visitDate: String, val memo: String, val photoUri: String)

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TravelDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Travel"
        private const val COLUMN_NO = "no"
        private const val COLUMN_PLACE = "place"
        private const val COLUMN_VISIT_DATE = "visit_date"
        private const val COLUMN_MEMO = "memo"
        private const val COLUMN_PHOTO_URI = "photo_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PLACE + " TEXT, " + COLUMN_VISIT_DATE + " TEXT, " + COLUMN_MEMO + " TEXT, " + COLUMN_PHOTO_URI + " TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTravel(place: String, visit_date: String, memo: String, photo_uri: String): Long {
        val db = writableDatabase
        val values = ContentValues(). apply {
            put(COLUMN_PLACE, place)
            put(COLUMN_VISIT_DATE, visit_date)
            put(COLUMN_MEMO, memo)
            put(COLUMN_PHOTO_URI, photo_uri)
        }
        return  db.insert(TABLE_NAME, null, values)
    }

    fun updateTravel(no: Int, place: String, visit_date: String, memo: String, photo_uri: String): Int {
        val db = writableDatabase
        val values = ContentValues(). apply {
            put(COLUMN_PLACE, place)
            put(COLUMN_VISIT_DATE, visit_date)
            put(COLUMN_MEMO, memo)
            put(COLUMN_PHOTO_URI, photo_uri)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_NO = ?", arrayOf(no.toString()))
    }

    fun deleteTravel(no: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_NO = ?", arrayOf(no.toString()))
    }

    fun getAllTravel(): List<Travel> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_NO DESC")
        val travelList = mutableListOf<Travel>()
        with(cursor) {
            while (moveToNext()) {
                val no = getInt(getColumnIndexOrThrow(COLUMN_NO))
                val place = getString(getColumnIndexOrThrow(COLUMN_PLACE))
                val visit_date = getString(getColumnIndexOrThrow(COLUMN_VISIT_DATE))
                val memo = getString(getColumnIndexOrThrow(COLUMN_MEMO))
                val photo_uri = getString(getColumnIndexOrThrow(COLUMN_PHOTO_URI))
                travelList.add(Travel(no, place, visit_date, memo, photo_uri))
            }
            close()
        }
        return travelList
    }
}