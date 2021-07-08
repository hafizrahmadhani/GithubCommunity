package com.hafizrahmadhani.github.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelFavUser(
    var id: Int = 0,
    var avatar_url : String? = null,
    var username : String? = null,
    var login : String
):Parcelable
