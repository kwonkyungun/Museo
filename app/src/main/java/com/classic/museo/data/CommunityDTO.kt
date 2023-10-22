package com.classic.museo.data

import java.sql.Timestamp


data class CommunityDTO(
    var uid:String,
    var title:String,
    var text:String,
    var date:Long,
    var UserId:String,
    var NickName:String,
){
    constructor() : this("","","",123L,"","")
}