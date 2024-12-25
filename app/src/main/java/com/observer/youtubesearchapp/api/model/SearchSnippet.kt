package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String? = null
)
