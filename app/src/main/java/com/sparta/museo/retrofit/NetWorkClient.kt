package com.sparta.museo.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


object NetWorkClient {

    private const val MUSEUM_BASE_URL = "http://api.data.go.kr/openapi/"


    private fun createOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()
    }


    private val museoRetrofit = Retrofit.Builder()
        .baseUrl(MUSEUM_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
            createOkHttpClient()
        ).build()

    val museoNetWork: NetWorkInterface = museoRetrofit.create(NetWorkInterface::class.java)

}