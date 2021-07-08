package com.hafizrahmadhani.github

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelRv(
    var avatar_url : String,
    var login : String,
): Parcelable
