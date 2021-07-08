package com.hafizrahmadhani.github.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.USER_NAME
import kotlinx.coroutines.InternalCoroutinesApi
import java.sql.SQLException
import kotlin.jvm.Throws

class FavoriteHelper (context: Context){

    companion object {
        private lateinit var DATABASEHELPER : DatabaseHelper
        private var INSTANCE : FavoriteHelper? = null
        private const val DB_TABLE = TABLE_NAME

        @InternalCoroutinesApi
        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this){
            INSTANCE ?: FavoriteHelper(context)
        }
        private lateinit var database : SQLiteDatabase
    }

    init {
        DATABASEHELPER = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = DATABASEHELPER.writableDatabase
    }

    fun close(){
        DATABASEHELPER.close()

        if(database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor{
        return database.query(
                DB_TABLE,
                null,
                "${DatabaseContract.FavColumns.FAV} = '1'",
                null,
                null,
                null,
                null,
                null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DB_TABLE,
                null,
                "$USER_NAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }

    fun insert(values: ContentValues?): Long {
        if (!database.isOpen) {
            open()
        }
        return database.insert(DB_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        if (!database.isOpen) {
            open()
        }
        return database.update(DB_TABLE, values, "$USER_NAME = ?", arrayOf(id))
    }
}