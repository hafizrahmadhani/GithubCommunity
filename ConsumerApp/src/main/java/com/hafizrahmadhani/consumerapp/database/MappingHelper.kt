package com.hafizrahmadhani.consumerapp.database

import android.database.Cursor
import com.hafizrahmadhani.consumerapp.datamodel.DataModelFavUser

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor : Cursor?): ArrayList<DataModelFavUser>{
        val favoriteList = ArrayList<DataModelFavUser>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavColumns.ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USER_NAME))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOGIN))
                favoriteList.add(DataModelFavUser(id, avatar, username, login))
            }
        }
        return favoriteList
    }
}