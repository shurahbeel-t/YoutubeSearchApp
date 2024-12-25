package com.observer.youtubesearchapp.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val totalResults: Long,
    val resultsPerPage: Long
)
