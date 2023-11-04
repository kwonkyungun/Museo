package com.classic.museo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikeDTO(
    val title:String,
    val address:String,
    val category:String,
    val museum: String,
) : Parcelable {
    constructor() : this("","","","")
}


