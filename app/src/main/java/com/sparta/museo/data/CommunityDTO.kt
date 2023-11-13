package com.sparta.museo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityDTO(
    var title:String,
    var text:String,
    var date:String,
    var UserId:String,
    var NickName:String,
    var museum:String,
    var uid:String,

) : Parcelable {
    constructor() : this("","","","","","","")
}