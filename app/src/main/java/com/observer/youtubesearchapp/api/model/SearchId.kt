package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchId(
    val kind: String,
    val videoId: String? = null,
    val channelId: String? = null,
    val playlistId: String? = null
)
