package com.observer.youtubesearchapp.model

sealed interface VideoStatus {
    object Live : VideoStatus
    object Uploaded : VideoStatus
}