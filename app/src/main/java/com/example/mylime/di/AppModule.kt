package com.example.mylime.di

import com.example.mylime.data.RetrofitService
import com.example.mylime.repository.BeersRepository
import com.example.mylime.repository.BeersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        @Singleton
        fun providesRetrofit(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            return Retrofit.Builder().baseUrl("https://api.punkapi.com/v2/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
        }


        @Provides
        @Singleton
        fun providerBooksService(retrofit: Retrofit): RetrofitService =
            retrofit.create(RetrofitService::class.java)

    }

    @Singleton
    @Binds
    abstract fun bindsRepository(settingsRepository: BeersRepositoryImpl): BeersRepository
}