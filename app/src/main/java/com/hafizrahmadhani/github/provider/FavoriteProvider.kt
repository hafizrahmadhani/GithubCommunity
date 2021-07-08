package com.hafizrahmadhani.github.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.hafizrahmadhani.github.database.DatabaseContract.USER
import com.hafizrahmadhani.github.database.FavoriteHelper
import kotlinx.coroutines.InternalCoroutinesApi

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val Fav = 1
        private const val Fav_Id = 2
        private lateinit var favoriteHelper : FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            sUriMatcher.addURI(USER, TABLE_NAME, Fav)
            sUriMatcher.addURI(USER, "$TABLE_NAME/#", Fav_Id)
        }
    }

    @InternalCoroutinesApi
    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when(sUriMatcher.match(uri)){
            Fav -> favoriteHelper.queryAll()
            Fav_Id -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added : Long = when(Fav){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }
}