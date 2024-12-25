package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
