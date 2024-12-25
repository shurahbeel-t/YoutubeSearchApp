package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail
)
