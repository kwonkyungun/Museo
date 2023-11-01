package com.classic.museo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class AnnouncementDTO(
    var title:String,
    var date:String,
    var uid:String,
    var text:String,

) : Parcelable{
    constructor() : this("","","","")
}

