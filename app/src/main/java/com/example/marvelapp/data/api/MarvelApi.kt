
package com.example.marvelapp.data.api

import com.example.marvelapp.data.model.MarvelResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String
    ): MarvelResponse
}
