
package com.example.marvelapp.data.repository

import android.content.Context
import com.example.marvelapp.data.model.MarvelResponse
import com.example.marvelapp.domain.model.Character
import com.example.marvelapp.domain.model.ResourceInfo
import com.example.marvelapp.domain.model.UrlInfo
import com.google.gson.Gson
import java.io.InputStreamReader

class CharacterRepository(private val context: Context) {

    suspend fun getCharacters(page: Int): List<Character> {
        val limit = 6
        val offset = page * limit

        return try {
            val inputStream = context.resources.openRawResource(
                context.resources.getIdentifier("marvel_characters", "raw", context.packageName)
            )
            val reader = InputStreamReader(inputStream)
            val response = Gson().fromJson(reader, MarvelResponse::class.java)

            val filteredResults = response.data.results.filter { 
                !it.thumbnail.path.contains("image_not_available") && 
                it.thumbnail.path.isNotEmpty() 
            }
            
            if (offset >= filteredResults.size) return emptyList()
            
            val end = (offset + limit).coerceAtMost(filteredResults.size)
            val paginatedList = filteredResults.subList(offset, end)

            paginatedList.map { dto ->
                Character(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    imageUrl = "${dto.thumbnail.path}.${dto.thumbnail.extension}",
                    comics = dto.comics?.items?.map { ResourceInfo(it.name, it.resourceURI) } ?: emptyList(),
                    series = dto.series?.items?.map { ResourceInfo(it.name, it.resourceURI) } ?: emptyList(),
                    stories = dto.stories?.items?.map { ResourceInfo(it.name, it.resourceURI) } ?: emptyList(),
                    events = dto.events?.items?.map { ResourceInfo(it.name, it.resourceURI) } ?: emptyList(),
                    urls = dto.urls?.map { UrlInfo(it.type, it.url) } ?: emptyList()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCharacterById(id: Int): Character? {
        return getCharacters(0).find { it.id == id }
    }
}
