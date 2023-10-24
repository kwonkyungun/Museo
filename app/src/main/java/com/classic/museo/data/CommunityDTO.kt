package com.classic.museo.data

import java.sql.Timestamp


data class CommunityDTO(
    var title:String,
    var text:String,
    var date:String,
    var UserId:String,
    var NickName:String,
    var museum:String,
    var UID:String,
){
    constructor() : this("","","","","","","")
}