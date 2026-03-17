
package com.example.marvelapp.domain.model

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val comics: List<ResourceInfo> = emptyList(),
    val series: List<ResourceInfo> = emptyList(),
    val stories: List<ResourceInfo> = emptyList(),
    val events: List<ResourceInfo> = emptyList(),
    val urls: List<UrlInfo> = emptyList()
)

data class ResourceInfo(
    val name: String,
    val imageUrl: String
)

data class UrlInfo(
    val type: String,
    val url: String
)
