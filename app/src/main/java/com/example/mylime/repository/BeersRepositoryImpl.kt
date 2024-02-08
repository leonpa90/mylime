package com.example.mylime.repository

import com.example.mylime.data.RetrofitService
import com.example.mylime.domain.mapper.toModel
import com.example.mylime.domain.model.Beer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BeersRepositoryImpl @Inject constructor(private val retrofitService: RetrofitService) :BeersRepository{
    override suspend fun getBeers(): Result<List<Beer>> = withContext(Dispatchers.IO) {
        runCatching {
            retrofitService.getBeers().body()?.map {
                it.toModel()
            }?: emptyList()
        }
    }

    override suspend fun getBeer(id:Int): Result<Beer?> = withContext(Dispatchers.IO){
        runCatching {
            retrofitService.getBeer(id).body()?.first()?.toModel()
        }
    }

}
