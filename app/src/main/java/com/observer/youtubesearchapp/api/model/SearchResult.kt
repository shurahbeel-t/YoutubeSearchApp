package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val kind: String,
    @SerialName(value = "etag")
    val eTag: String,
    val id: SearchId,
    val snippet: SearchSnippet
)
