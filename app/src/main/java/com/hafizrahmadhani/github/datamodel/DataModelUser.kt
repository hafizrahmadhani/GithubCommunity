package com.hafizrahmadhani.github.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelUser(
    var name: String,
    var login: String,
    val id: Int,
    var avatar_url: String,
    var following: Int,
    var followers: Int,
    val following_url: String,
    val followers_url: String,
    val repo: Int
): Parcelable
