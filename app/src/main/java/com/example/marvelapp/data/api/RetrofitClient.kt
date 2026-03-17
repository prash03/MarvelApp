
package com.example.marvelapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val api: MarvelApi by lazy {

        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }
}
