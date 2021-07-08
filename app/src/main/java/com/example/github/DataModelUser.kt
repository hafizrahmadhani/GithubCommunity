package com.example.github

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelUser (
    val imgUser : Int?,
    val name : String?,
    val userName : String?,
    val followers : String?,
    val following : String?,
    val repo : String?,
    val location : String?,
    val company : String?
): Parcelable
