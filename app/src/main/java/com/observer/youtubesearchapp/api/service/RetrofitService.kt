package com.observer.youtubesearchapp.api.service

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


private const val BASE_URL = "https://youtube.googleapis.com/youtube/v3/"
// private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"


/**
 * The following variable is to have the deserialization ignore unknown keys
 * i.e. JSON has keys, but dataclass doesn't
 * if you do not want to use this, then just use Json.asConverterFactory instead
 */
val json: Json = Json {
    ignoreUnknownKeys = true
}


fun getYoutubeApiService(): YoutubeDataApi = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
    .build()
    .create(YoutubeDataApi::class.java)


object YoutubeApi {
    val retrofitService: YoutubeDataApi by lazy {
        getYoutubeApiService()
    }
}