package com.observer.youtubesearchapp.model

data class SearchResultEntry(
    val title: String,
    val imgUrl: String,
    val status: VideoStatus,
    val timeOrCount: String,
    val date: String
)
