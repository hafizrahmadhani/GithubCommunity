package com.hafizrahmadhani.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val USER = "com.hafizrahmadhani.github"
    const val SCHEME = "content"

    internal class FavColumns : BaseColumns {
        companion object{
            private const val TABLE_NAME = "favorite"
            const val ID = "id"
            const val AVATAR = "img_userfav"
            const val USER_NAME = "user_name"
            const val LOGIN = "login"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME).authority(USER).appendPath(TABLE_NAME).build()
        }
    }
}