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
    val pageNo: Int,    //페이지번호
    val numOfRows: Int  //한 페이지 결과 수
)

data class MuseoHeader(
    val resultCode: String,
    val resultMsg: String
)

data class Record(
    val fcltyNm : String,   //시설명
    val fcltyType : String, //공립/사립구분
    val rdnmadr : String,   //소재지도로명주소
    val lnmadr : String,    //소재지지번주소
    val latitude : String,  //위도
    val longitude : String, //경도
    val operPhoneNumber : String,   //운영기관전화번호
    val operInstitutionNm : String, //운영기관명
    val homepageUrl : String,       //운영홈페이지
    val fcltyInfo : String,         //편의시설정보
    val weekdayOperOpenHhmm : String,   //평일관람시작시각
    val weekdayOperColseHhmm : String,  //평일관람종료시각
    val holidayOperOpenHhmm : String,   //공휴일관람시작시각
    val holidayCloseOpenHhmm : String,  //공휴일관람종료시각
    val rstdeInfo : String,     //휴관정보
    val adultChrge : String,    //어른관람료
    val yngbgsChrge : String,   //청소년관람료
    val childChrge : String,    //어린이관람료
    val etcChrgeInfo : String?, //관람료기타정보
    val fcltyIntrcn : String?,  //박물관미술관소개
    val trnsportInfo : String?, //교통안내정보
    val phoneNumber : String,   //관리기관전화번호
    val institutionNm : String, //관리기관명
    val referenceDate : String, //데이터기준일자
    val instt_code : String,    //제공기관코드

    var viewType : Int = 1
)
