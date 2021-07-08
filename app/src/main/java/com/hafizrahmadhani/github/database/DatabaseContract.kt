package com.hafizrahmadhani.github.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val USER = "com.hafizrahmadhani.github"
    const val SCHEME = "content"

    internal class FavColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "favorite"
            const val ID = "id"
            const val AVATAR = "img_userfav"
            const val USER_NAME = "user_name"
            const val LOGIN = "login"
            const val FAV = "fav"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME).authority(USER).appendPath(TABLE_NAME).build()
        }
    }
}