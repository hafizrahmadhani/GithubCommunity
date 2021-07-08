package com.hafizrahmadhani.github.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object{
        private const val DB_NAME = "dbfavoriteuser"
        private const val DB_VERSION = 3

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavColumns.ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.FavColumns.USER_NAME} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.LOGIN} TEXT NOT NULL UNIQUE," +
                "${DatabaseContract.FavColumns.AVATAR} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.FAV} BOOLEAN)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}