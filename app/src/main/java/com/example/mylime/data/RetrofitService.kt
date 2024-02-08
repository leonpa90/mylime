package com.example.mylime.data

import com.example.mylime.model.Response
import com.example.mylime.model.ResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("beers")
    suspend fun getBeers():retrofit2.Response<Response>

    @GET("beers/{id}")
    suspend fun getBeer(@Path("id") id:Int):retrofit2.Response<List<ResponseItem>>
}