package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchListResponse(
    val kind: String,
    @SerialName(value = "etag")
    val eTag: String,
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val regionCode: String? = null,
    val pageInfo: PageInfo,
    val items: List<SearchResult>
)
