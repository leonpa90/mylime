package com.example.mylime.repository

import com.example.mylime.data.RetrofitService
import com.example.mylime.model.ResponseItem
import javax.inject.Inject

class BeersRepositoryImpl @Inject constructor(private val retrofitService: RetrofitService) :BeersRepository{
    override suspend fun getBeers(): List<ResponseItem> {
       return retrofitService.getBeers()
    }

    override suspend fun getBeer(id:Int): ResponseItem {
        return retrofitService.getBeer(id).first()
    }

}
