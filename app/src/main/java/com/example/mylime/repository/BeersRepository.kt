package com.example.mylime.repository

import com.example.mylime.model.ResponseItem

interface BeersRepository {
    suspend fun getBeers(): List<ResponseItem>
    suspend fun getBeer(id:Int): ResponseItem
}