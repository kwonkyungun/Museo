package com.classic.museo.data


import com.google.gson.annotations.SerializedName

data class Museo(val response: MuseoResponse)

data class MuseoResponse(
    @SerializedName("body")
    val museoBody: MuseoBody,
    @SerializedName("header")
    val museoHeader: MuseoHeader
)

data class MuseoBody(
    val totalCount:Int,
    @SerializedName("items")
    val museoItem: MutableList<Record>?,
    val pageNo: Int,
    val numOfRows: Int
)

data class MuseoHeader(
    val resultCode: String,
    val resultMsg: String
)

data class Record(
    val fcltyNm : String,
    val fcltyType : String,
    val rdnmadr : String,
    val lnmadr : String,
    val latitude : String,
    val longitude : String,
    val operPhoneNumber : String,
    val operInstitutionNm : String,
    val homepageUrl : String,
    val fcltyInfo : String,
    val weekdayOperOpenHhmm : String,
    val weekdayOperColseHhmm : String,
    val holidayOperOpenHhmm : String,
    val holidayCloseOpenHhmm : String,
    val rstdeInfo : String,
    val adultChrge : String,
    val yngbgsChrge : String,
    val childChrge : String,
    val etcChrgeInfo : String?,
    val fcltyIntrcn : String?,
    val trnsportInfo : String?,
    val phoneNumber : String,
    val institutionNm : String,
    val referenceDate : String,
    val instt_code : String,
)
