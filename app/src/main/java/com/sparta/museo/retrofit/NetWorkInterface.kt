package com.sparta.museo.retrofit

import com.sparta.museo.data.Museo
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NetWorkInterface {
    @GET("tn_pubr_public_museum_artgr_info_api")
    suspend fun getMuseo(@QueryMap param: HashMap<String, String>): Museo
}
