package com.example.mylime.repository

import com.example.mylime.domain.model.Beer

interface BeersRepository {
    suspend fun getBeers(): Result<List<Beer>>
    suspend fun getBeer(id:Int): Result<Beer?>
}