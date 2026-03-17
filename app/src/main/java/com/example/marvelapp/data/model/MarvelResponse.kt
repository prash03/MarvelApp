
package com.example.marvelapp.data.model

data class MarvelResponse(
    val data: DataContainer
)

data class DataContainer(
    val results: List<CharacterDto>
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    val comics: ResourceList? = null,
    val series: ResourceList? = null,
    val stories: ResourceList? = null,
    val events: ResourceList? = null,
    val urls: List<MarvelUrl>? = null
)

data class Thumbnail(
    val path: String,
    val extension: String
)

data class ResourceList(
    val items: List<ResourceItem>
)

data class ResourceItem(
    val resourceURI: String,
    val name: String
)

data class MarvelUrl(
    val type: String,
    val url: String
)
