package com.observer.youtubesearchapp.api.service

import com.observer.youtubesearchapp.api.model.SearchListResponse
import com.observer.youtubesearchapp.keys.YoutubeDataApiKeyRestricted
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeDataApi {
    @GET("search?part=snippet&type=video&key=${YoutubeDataApiKeyRestricted}")
    suspend fun getSearchResponseVideos(@Query("q") query: String, @Query("maxResults") maxResults: Long): Response<SearchListResponse>
}